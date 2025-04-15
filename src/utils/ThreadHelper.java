package utils;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ThreadHelper {

    public static<T, R> R runStreamSafe(Collection<T> source, Function<Stream<T>, R> action)  {
        synchronized (source) {
            return action.apply(source.stream());
        }
    }

    public static<R> R runStreamSafe(Object lock, Supplier<Stream<?>> steamSupl, Function<Stream<?>, R> action) {
        synchronized (lock) {
            return action.apply(steamSupl.get());
        }
    }
}
