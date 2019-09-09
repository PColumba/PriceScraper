import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

class Logger {

    private static final Logger loggerInstance = new Logger();

    private static final String LOGGER_DEFAULT_FILE_PATH = "log";

    private static final int SECTION_TITLEBAR_LENGTH = 80;

    private static final String TITLEBAR_SYMBOL = "-";

    private static final String SECTION_INDICATOR = "* ";

    private static final String INDENTATION = "    ";

    private StringBuilder currentLog = new StringBuilder(1000);

    private Logger() {}

    static Logger getLogger() {
        return loggerInstance;
    }

    Path saveLogFile(){
        String dateString = (new SimpleDateFormat("dd.MM.YYYY_HH.mm")).format(new Date());
        String fileName = "log_" + dateString;
        Path filePath = null;
        try {
            filePath = FileSystems.getDefault().getPath(LOGGER_DEFAULT_FILE_PATH).resolve(fileName);
            Files.createFile(filePath);
            Files.writeString(filePath,currentLog);
        } catch (Exception e) {
            System.out.println("Something went wrong when trying to create a log file: " + e.getMessage());
            e.printStackTrace();
        }
        return filePath;
    }

    Logger addTitle(String title){
        currentLog.append(TITLEBAR_SYMBOL.repeat(SECTION_TITLEBAR_LENGTH)).append(System.lineSeparator());
        currentLog.append(title).append(System.lineSeparator()).append(TITLEBAR_SYMBOL.repeat(SECTION_TITLEBAR_LENGTH)).append(System.lineSeparator());
        return loggerInstance;
    }

    Logger addSection(String sectionHeader){
        currentLog.append(SECTION_INDICATOR).append(sectionHeader).append(System.lineSeparator()).append(INDENTATION);
        return loggerInstance;
    }

    Logger addToSection(String message){
        currentLog.append(message).append(System.lineSeparator()).append(INDENTATION);
        return loggerInstance;
    }

    Logger closeSection(){
        currentLog.append(System.lineSeparator());
        return loggerInstance;
    }

    Logger closeTitle(){
        currentLog.append(TITLEBAR_SYMBOL.repeat(SECTION_TITLEBAR_LENGTH)).append(System.lineSeparator().repeat(3));
        return loggerInstance;
    }
}