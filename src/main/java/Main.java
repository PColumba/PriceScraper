import org.jsoup.nodes.Document;

import javax.mail.internet.MimeMessage;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args){


        Logger logger = Logger.getLogger();
        long start = System.nanoTime();
        long end, executionTimeInMinutes;

        logger.addTitle("Starting program execution");

        List<Job> jobList = getJobList(args);
        if(jobList.isEmpty()) {
            System.out.println("Wrong arguments, provide valid job id or leave blank(equals to run all)");
            System.exit(0);
        }

        for(Job job : jobList){
            logger.addTitle("Starting job: " + job.getJobId());
            List<Document> docs = Fetcher.getPages(job);
            List<PriceInstance> jobPriceInstances = Parser.parse(docs,job);
            DataBaseConnector.updateTablesWithPriceData(jobPriceInstances, job);
            logger.closeTitle();
        }

        end = System.nanoTime() - start;
        executionTimeInMinutes = end / (1000000000L * 60);
        logger.addSection("Finished, Execution time[m]: " + executionTimeInMinutes).closeSection().closeTitle();
        Path pathToLogFile = logger.saveLogFile();

        //send log file over email;
        String dateString = (new SimpleDateFormat("dd.MM.YYYY_HH:mm")).format(new Date());
        try {
            MimeMessage mimeMessage = SendEmail.createEmailWithAttachment("pawel.agolab@gmail.com","pawel.agolab@gmail.com","Price scrape report "  + dateString,
                    "This is an autogenerated email with price scrape log for date " + dateString,pathToLogFile);
            SendEmail.sendMessage(mimeMessage);
        } catch (Exception e) {
            System.out.println("Something went wrong when trying to send autogenerated email");
            e.printStackTrace();
        }
    }

    private static List<Job> getJobList(String[] args){
        if(args.length == 0)
            return Config.getJobs();
        else
            return Config.getJobs().stream().filter(job -> Arrays.asList(args).contains(job.getJobId())).collect(Collectors.toList());
    }
}
