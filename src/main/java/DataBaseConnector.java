import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;

public class DataBaseConnector {

    private DataBaseConnector() {}

    private static Logger logger = Logger.getLogger();

    private static String createUpdateStatement(List<PriceInstance> priceInstances, Job job){
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("INSERT INTO ").append(job.getJobId()).append("(product_id,product_name,price,price_type) VALUES");
        for (PriceInstance priceInstance : priceInstances) {
            statementBuilder.append("('")
                    .append(priceInstance.getProductId()).append("',\"")
                    .append(priceInstance.getProductName().replaceAll("\"", Matcher.quoteReplacement("'"))).append("\",'")
                    .append(priceInstance.getPriceValue()).append("','")
                    .append(priceInstance.getPriceType()).append("'),\n");
        }
        //Remove last comma
        statementBuilder.delete(statementBuilder.length() - 2, statementBuilder.length());
        return statementBuilder.toString();
    }

    public static Connection getConnection() throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(Config.DATABASE_URL);
        return dataSource.getConnection();
    }

    private static boolean ifTableExists(Job job){
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(Config.DATABASE_URL);
        try (Connection conn = dataSource.getConnection()){
            PreparedStatement ifExistsStatement = conn.prepareStatement("SELECT name FROM sqlite_master WHERE type=\"table\" AND name=?");
            ifExistsStatement.setString(1, job.getJobId());
            ResultSet resultSet = ifExistsStatement.executeQuery();
            return resultSet.next();
        } catch(Exception e){
            e.printStackTrace();
            logger.addToSection("Something went wrong when checking table existence for job: " + job.getJobId() + " Error message: " + e.toString());
            return false;
        }
    }

    private static void createTable(Job job){
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(Config.DATABASE_URL);
        try (Connection conn = dataSource.getConnection()){
            conn.createStatement().executeUpdate("CREATE TABLE " + job.getJobId() + "(auto_id INTEGER PRIMARY KEY, product_id TEXT NOT NULL, product_name TEXT, " +
                    "price INTEGER, price_type CHECK(price_type IN ('KG','UNIT','L','NOT_SPECIFIED')), " +
                    "date TEXT NOT NULL DEFAULT(datetime('now','localtime')))");
        } catch(Exception e){
            logger.addToSection("Something went wrong when creating table for job: " + job.getJobId() + " Error message: " + e.toString());
            e.printStackTrace();
        }
    }

    static void updateTablesWithPriceData(List<PriceInstance> priceInstances, Job job){
        logger.addSection("Starting saving data to database").addToSection("Creating update statement");
        if(priceInstances.isEmpty()) {
            logger.addToSection("No price instances to save in database").closeSection();
            return;
        }
        String updateQuery = createUpdateStatement(priceInstances, job);
        logger.addToSection("Successfully created update statement");
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(Config.DATABASE_URL);
        logger.addToSection("Opening connection to database");
        if(!ifTableExists(job))
            createTable(job);
        try (Connection conn = dataSource.getConnection()){
            conn.createStatement().executeUpdate(updateQuery);
            logger.addToSection("Successfully updated database for job: " + job.getJobId()).closeSection();
        } catch(Exception e){
            logger.addToSection("Something went wrong when connecting/executing update,job is: " + job.getJobId() + " Error message: " + e.toString()).closeSection();
            e.printStackTrace();
        }

    }
}



