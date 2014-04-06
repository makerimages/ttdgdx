package trappedtildoom.core.util;

import trappedtildoom.core.service.AbsoluteLogger;

public class SleepUtil {
    public static void Sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            AbsoluteLogger.severe(null, e);
        }
    }
}
