package utils;

import java.time.LocalDateTime;

public class Logger {
    private int second = 0;
    private int minute = 0;
    public static void log(String log) {
        log(log, false);
    }
    
    public static void log(String log, boolean error) {
        LocalDateTime time = LocalDateTime.now();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();
        int milli = time.getNano()/1000;
        if (error) System.err.printf("[LOGGER/%d:%d:%d:%d] " + log + "%n", hours, minutes, seconds, milli);
        else System.out.printf("[LOGGER/%d:%d:%d:%d] " + log + "%n", hours, minutes, seconds, milli);
    }
}
