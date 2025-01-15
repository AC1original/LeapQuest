package utils;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class Logger {

    public static<T> void info(T clazz, String log) {
        log(clazz, log, LogLevel.INFO);
    }
    public static void info(Class<?> clazz, String log) {
        log(clazz, log, LogLevel.INFO);
    }

    public static<T> void warn(T clazz, String log) {
        log(clazz, log, LogLevel.WARNING);
    }
    public static void warn(Class<?> clazz, String log) {
        log(clazz, log, LogLevel.WARNING);
    }

    public static<T> void error(T clazz, String log) {
        log(clazz, log, LogLevel.ERROR);
    }
    public static void error(Class<?> clazz, String log) {
        log(clazz, log, LogLevel.ERROR);
    }

    public static<T> void log(T clazz, String log, LogLevel level) {
        log(clazz.getClass(), log, level);
    }
    public static<T> void log(@Nullable Class<?> clazz, String log, LogLevel level) {
        LocalDateTime time = LocalDateTime.now();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();
        int milli = time.getNano()/1000;
        String message = String.format("[LOGGER/%s/%d:%d:%d:%d] %s %s", level.name(), hours, minutes, seconds, milli,
                clazz == null ? "" : clazz.getSimpleName() + ": ", log);

        if (level.error) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }


    }

    public enum LogLevel {
        INFO(false),
        WARNING(true),
        ERROR(true);

        final boolean error;
        LogLevel(boolean error) {
            this.error = error;
        }
    }
}
