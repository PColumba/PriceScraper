import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DebugMethods {

    static void testProcess(Job job){
        List<Document> docs = new ArrayList<>();
        docs.add(getPage(job));
        List<PriceInstance> ps = Parser.parse(docs, job);
        System.out.println(ps.toString());
    }

    private static Document getPage(Job job){
        try {
            return Jsoup.connect(job.getBaseURL() + job.getCategories()[2] + "?" + job.getPageParam() + "=" + 0).get();
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return null;
        }
    }

}
