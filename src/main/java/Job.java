import java.util.Arrays;

public class Job {

    Job(String jobId, String baseURL, String pageParam, String pageCountUrlExtension, int pageOffset, Extractor priceValueExtractor, Extractor priceTypeExtractor,
        Extractor productIdExtractor, Extractor productNameExtractor, Extractor productsListExtractor, Extractor pageCountExtractor, String[] categories) {
        this.jobId = jobId;
        this.baseURL = baseURL;
        this.pageParam = pageParam;
        this.pageCountURLExtension = pageCountUrlExtension;
        this.pageOffset = pageOffset;
        this.priceValueExtractor = priceValueExtractor;
        this.priceTypeExtractor = priceTypeExtractor;
        this.productIdExtractor = productIdExtractor;
        this.productNameExtractor = productNameExtractor;
        this.productsListExtractor = productsListExtractor;
        this.pageCountExtractor = pageCountExtractor;
        this.categories = categories;
    }

    private final String jobId;
    private final String baseURL;
    private final String pageParam;
    private final String pageCountURLExtension;
    private final int pageOffset;
    private final Extractor priceValueExtractor;
    private final Extractor priceTypeExtractor;
    private final Extractor productIdExtractor;
    private final Extractor productNameExtractor;
    private final Extractor productsListExtractor;
    private final Extractor pageCountExtractor;
    private final String[] categories;

    String getJobId() {
        return jobId;
    }

    String getBaseURL() {
        return baseURL;
    }

    String getPageParam() {
        return pageParam;
    }

    String getPageCountUrlExtension() {
        return pageCountURLExtension;
    }

    int getPageOffset() { return pageOffset; }

    Extractor getPriceValueExtractor() {
        return priceValueExtractor;
    }

    Extractor getPriceTypeExtractor() {
        return priceTypeExtractor;
    }

    Extractor getProductIdExtractor() {
        return productIdExtractor;
    }

    Extractor getProductNameExtractor() {
        return productNameExtractor;
    }

    Extractor getProductsListExtractor() {
        return productsListExtractor;
    }

    Extractor getPageCountExtractor() {
        return pageCountExtractor;
    }

    String[] getCategories() {
        return categories;
    }

    private String categoriesToString(){
        return Arrays.asList(categories).toString();
    }

    @Override
    public String toString(){
        return "jobID: " + getJobId() + System.lineSeparator() +
                "baseURL: " + getBaseURL() + System.lineSeparator() +
                "priceValueExtractor: " + priceValueExtractor.toString() + System.lineSeparator() +
                "priceTypeExtractor: " + priceTypeExtractor.toString() + System.lineSeparator() +
                "productIdExtractor: " + productIdExtractor.toString() + System.lineSeparator() +
                "productNameExtractor: " + productNameExtractor.toString() + System.lineSeparator() +
                "productsListExtractor: " + productsListExtractor.toString() + System.lineSeparator() +
                "pageCountExtractor: " + pageCountExtractor.toString() + System.lineSeparator() +
                "categories: " + categoriesToString();
    }
}
