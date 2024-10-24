package utils;
import java.time.LocalDateTime;

public class Logger {

    public static void log(String log) {
        log(log, false, null);
    }
    public static void log(Class<?> clazz, String log) {
        log(log, false, clazz);
    }
    public static void log(String log, boolean error) {
        log(log, error, null);
    }

    public static void log(String log, boolean error, Class<?> clazz) {
        LocalDateTime time = LocalDateTime.now();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();
        int milli = time.getNano()/1000;
        if (log.endsWith(".")) {
            log = log.substring(0, log.length()-1);
        }

        if (clazz != null) {
            if (error) System.err.printf("[LOGGER/%d:%d:%d:%d] " + clazz.getSimpleName() + ": " + log + "%n", hours, minutes, seconds, milli);
            else System.out.printf("[LOGGER/%d:%d:%d:%d] " + clazz.getSimpleName() + ": " + log + "%n", hours, minutes, seconds, milli);
        } else {
            if (error) System.err.printf("[LOGGER/%d:%d:%d:%d] " + log + "%n", hours, minutes, seconds, milli);
            else System.out.printf("[LOGGER/%d:%d:%d:%d] " + log + "%n", hours, minutes, seconds, milli);
        }
    }
}
