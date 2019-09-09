import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.List;

class Fetcher {

    private Fetcher(){ }

    private static final long REQUEST_DELAY = 200;
    private static final int REQUEST_TIMEOUT = 60000;

    static List<Document> getPages(Job job){
        List<Document> docList = new ArrayList<>();
        Logger.getLogger().addSection("Starting pages download for job " + job.getJobId());
        for(String category : job.getCategories()) {
            int pages = getCategoryPageCount(job, category);
            Logger.getLogger().addToSection("Category  " + category + " has " + pages + " pages");
            for (int i = job.getPageOffset(); i < pages + job.getPageOffset(); i++){
                try {
                    Thread.sleep(REQUEST_DELAY);
                    Document doc = Jsoup.connect(job.getBaseURL() + category + "?" + job.getPageParam() + "=" + i).timeout(REQUEST_TIMEOUT).get();
                    docList.add(doc);
                } catch (Exception e) {
                    Logger.getLogger().addToSection("!Error - Failed to download page " + i + " for category: " + category + ". Error Message: " + e.toString());
                }
            }
        }
        Logger.getLogger().addToSection("Finished downloading " + docList.size() +" pages for total of " + job.getCategories().length + " categories").closeSection();
        return docList;
    }

    private static int getCategoryPageCount(Job job, String category){
        try {
            String URL = job.getBaseURL() + category + (job.getPageCountUrlExtension() != null ? job.getPageCountUrlExtension() : "");
            Document doc = Jsoup.connect(URL).timeout(REQUEST_TIMEOUT).get();
            return Integer.parseInt(job.getPageCountExtractor().extract(doc));
        } catch (Exception e) {
            Logger.getLogger().addToSection("!Error - Failed to get page count for category: " + category + ". Error Message: " + e.toString());
            return -1;
        }
    }
}

