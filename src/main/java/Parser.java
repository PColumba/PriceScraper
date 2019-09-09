import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

class Parser {

    // assertions: String expression returned by extractor is ready as is in case of productName, productID. It is
    // further parsed to enum in case of priceType and in case of priceValue it is parsed to int with replacement of "," and ".",
    // as such priceValue String returned from extractor need only contain numbers and separators (, and .).

    static List<PriceInstance> parse(List<Document> docs, Job job){

        Logger.getLogger().addSection("Starting page parsing for job: " + job.getJobId());

        List<PriceInstance> priceInstances = new ArrayList<>();
        for(Document doc : docs){
            Elements products = job.getProductsListExtractor().getAll(doc);
            Logger.getLogger().addToSection("Parsing document page: " + doc.baseUri() + " number of products on page: " + products.size());
            for(Element el : products) {
                String productName;
                String productId;
                int priceValue;
                PriceInstance.PriceType priceType;

                try {
                    productName = job.getProductNameExtractor().extract(el);
                    productId = job.getProductIdExtractor().extract(el);
                    String extr;
                    if((extr = job.getPriceValueExtractor().extract(el)) != null)
                        priceValue = Integer.parseInt(extr.replaceAll("[.,\\s]", ""));
                    else
                        priceValue = PriceInstance.MISSING_PRICE;
                    if((extr = job.getPriceTypeExtractor().extract(el)) != null)
                        priceType = parsePriceType(extr);
                    else
                        priceType = PriceInstance.PriceType.NOT_SPECIFIED;

                    priceInstances.add(new PriceInstance(productId != null ? productId : PriceInstance.MISSING_PRODUCT_NAME,
                            productName != null ? productName : PriceInstance.MISSING_PRODUCT_NAME, priceValue, priceType));
                } catch (Exception e) {
                    Logger.getLogger().addToSection("!Error - Failed to create price instance for el: " + job.getProductNameExtractor().extract(el) + " for doc: " + doc.location() + ". Error Message: " + e.toString());
                }

            }
        }
        Logger.getLogger().addToSection("Finished parsing pages, price collected for : " + priceInstances.size() + " products").closeSection();
        return priceInstances;
    }

    private static PriceInstance.PriceType parsePriceType(String priceTypeString){
        if(priceTypeString.matches(".*(kg|Kg|KG).*"))
            return PriceInstance.PriceType.KG;
        if(priceTypeString.matches(".*(szt\\.|Szt\\.|SZT\\.|szt|Szt|SZT).*"))
            return PriceInstance.PriceType.UNIT;
        if(priceTypeString.matches(".*(l|L|l\\.|L\\.)*"))
            return PriceInstance.PriceType.L;
        return PriceInstance.PriceType.NOT_SPECIFIED;
    }

}

