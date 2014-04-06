package trappedtildoom.core.service;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class AbsoluteLogger {

    private static final Logger instance;

    static {
        LogManager.getLogManager().reset();

        instance = Logger.getLogger("AbsoluteLogger");
        instance.setLevel(Level.ALL);

        instance.addHandler(new ConsoleHandler());

        File logsDirectory = new File("logs");
        if (!logsDirectory.exists())
            logsDirectory.mkdir();

        try {
            File directory = new File("logs");
            if (!directory.exists())
                directory.mkdir();

            instance.addHandler(new FileHandler("logs/AbsoluteLogger.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AbsoluteLogger() {}

    public static void info(String message) {
        instance.info(message);
    }

    public static void severe(String message) {
        severe(message, null);
    }

    public static void severe(String message, Exception exception) {
        instance.log(Level.SEVERE, message, exception);
    }
}
