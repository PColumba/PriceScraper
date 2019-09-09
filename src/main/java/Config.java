import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Config {

    private static final List<Job> JOBS;
    static final String DATABASE_URL = "jdbc:sqlite:database/prices.db";

    private Config() {}

    static {
        List<JSONObject> jobFilesJSON = new ArrayList<>();
        List<Job> jobsModifiable = new ArrayList<>();
        Logger logger = Logger.getLogger();
        logger.addTitle("Starting jobs initialization").addSection("Reading config files");
        try {
            Path pathToJobFiles = FileSystems.getDefault().getPath("jobs");
            Object[] jobFilesPaths = Files.list(pathToJobFiles).toArray();
            for(Object jobFilePath : jobFilesPaths){
                String jobFileJSONContent = Files.lines((Path) jobFilePath).reduce("",(a,b) -> (a + b + System.lineSeparator()));
                jobFilesJSON.add(new JSONObject(jobFileJSONContent));
            }
            logger.addToSection("Reading successful").closeSection();
        } catch (IOException e) {
            logger.addSection("Initialization failed: " + e.getMessage() + System.lineSeparator() + "Terminating program execution").closeSection().saveLogFile();
            e.printStackTrace();
            System.exit(0);
        }
        try {
            logger.addSection("Parsing config files");
            for (JSONObject jobFileJSON : jobFilesJSON) {
                String jobID = jobFileJSON.getString("jobId");
                String baseURL = jobFileJSON.getString("baseURL");
                String pageParam = jobFileJSON.getString("pageParam");
                String pageCountURLExtension = jobFileJSON.has("pageCountURLExtension") ? jobFileJSON.getString("pageCountURLExtension") : null;
                int pageOffset = jobFileJSON.getInt("pageOffset");

                Extractor priceValueExtractor = parseExtractorObject(jobFileJSON.getJSONObject("priceValueExtractor"));
                Extractor priceTypeExtractor = parseExtractorObject(jobFileJSON.getJSONObject("priceTypeExtractor"));
                Extractor productIdExtractor = parseExtractorObject(jobFileJSON.getJSONObject("productIdExtractor"));
                Extractor productNameExtractor = parseExtractorObject(jobFileJSON.getJSONObject("productNameExtractor"));
                Extractor productsListExtractor = parseExtractorObject(jobFileJSON.getJSONObject("productsListExtractor"));
                Extractor pageCountExtractor = parseExtractorObject(jobFileJSON.getJSONObject("pageCountExtractor"));

                JSONArray categoriesJArr = jobFileJSON.getJSONArray("categories");
                String[] categories = new String[categoriesJArr.length()];
                for(int i = 0; i < categories.length; i++)
                    categories[i] = categoriesJArr.getString(i);

                jobsModifiable.add(new Job(jobID, baseURL, pageParam, pageCountURLExtension, pageOffset,priceValueExtractor, priceTypeExtractor, productIdExtractor,
                        productNameExtractor, productsListExtractor, pageCountExtractor,categories));
            }
            logger.addToSection("Parsing successful").closeSection();
            logger.addSection("Job initialization successful").closeSection().closeTitle();
        } catch (JSONException e){
            logger.addSection("Initialization failed: " + e.getMessage() + System.lineSeparator() + "Terminating program execution").closeSection().saveLogFile();
            e.printStackTrace();
            System.exit(0);
        }
        JOBS = Collections.unmodifiableList(jobsModifiable);
    }
    static List<Job> getJobs(){
        return JOBS;
    }

    private static Extractor parseExtractorObject(JSONObject jsonObject){
        String elementSelector = jsonObject.has("elementSelector") ? jsonObject.getString("elementSelector") : null;
        String elementTextPattern = jsonObject.has("elementTextPattern") ? jsonObject.getString("elementTextPattern") : null;
        boolean elementChildText = jsonObject.has("elementChildText") && jsonObject.getBoolean("elementChildText");
        String attributeName = jsonObject.has("attributeName") ? jsonObject.getString("attributeName") : null;
        String attributeTextPattern = jsonObject.has("attributeTextPattern") ? jsonObject.getString("attributeTextPattern") : null;
        boolean extractLastElement = jsonObject.has("extractLastElement") && jsonObject.getBoolean("extractLastElement");
        return new Extractor(elementSelector, elementTextPattern, elementChildText, attributeName, attributeTextPattern, extractLastElement);
    }
}
