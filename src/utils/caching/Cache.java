package utils.caching;

import main.GamePanel;
import org.jetbrains.annotations.NotNull;
import utils.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

//TODO: General Cache improvements (Structure and function)
public final class Cache<T> {
    private final int delTime, maxIndex;
    private final TimeUnit timeUnit;
    private final boolean unusedDelete, oldestIndexDelete, timeoutDelete;
    private final Map<String, CachedObject<T>> cached = Collections.synchronizedMap(new LinkedHashMap<>());
    private CachedObject<T> lastExpired;

    private Cache(int delTime, TimeUnit timeUnit, boolean unusedDelete, boolean oldestDelete, int maxIndex, boolean timeoutDelete) {
        this.delTime = delTime;
        this.timeUnit = timeUnit;
        this.unusedDelete = unusedDelete;
        this.oldestIndexDelete = oldestDelete;
        this.maxIndex = maxIndex;
        this.timeoutDelete = timeoutDelete;
    }

    public void tick() {
       if (timeoutDelete) {
           cached.forEach((k, v) -> {
               if (System.currentTimeMillis() - (unusedDelete ? v.getLastUsed() : v.getAdded()) > timeUnit.toMillis(delTime)) {
                   remove(k);
               }
           });
       }

       if (oldestIndexDelete && cached.size() > maxIndex) {
           remove(cached.keySet().iterator().next());
       }
    }

    public void replace(String key, T object) {
        cached.replace(key, new CachedObject<>(object));
    }

    public void add(String key, T object) {
        cached.put(key, new CachedObject<>(object));
    }

    public boolean contains(String key) {
        return cached.containsKey(key);
    }

    public void remove(T object) {
        cached.forEach((k, v) -> {
            if (v.getObject().equals(object)) {
                lastExpired = v;
                remove(k);
            }
        });
    }

    //TODO: Fix and improve onExpire methode
    public void onExpire(Consumer<CachedObject<T>> callback) {
        callback.accept(lastExpired);
    }

    public void remove(String key) {
        remove(cached.get(key).getObject());
    }

    public T get(String key) {
        return cached.get(key).getObject();
    }

    public Stream<CachedObject<T>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(cached.values(), 0), false);
    }

    public void forEach(@NotNull Consumer<? super CachedObject<T>> action) {
        Objects.requireNonNull(action);
        cached.values().forEach(action);
    }

    public List<CachedObject<T>> copyOfCachedObjects() {
        return new ArrayList<>(cached.values());
    }

    public Map<String, CachedObject<T>> copyOfCachedKeysAndObject() {
        return new HashMap<>(cached);
    }

    public static final class CacheBuilder<C> {
        private static final Set<Cache<?>> caches = new HashSet<>();
        private static boolean running = false;
        private int delTime = 10, maxIndex = 0;
        private TimeUnit timeUnit = TimeUnit.MINUTES;
        private boolean unusedDelete = false, oldestIndexDelete = false, timeoutDelete = false;

        private static void tick() {
            running = !running;

            if (running) {
                new Thread(() -> {
                    while (running) {
                        try {
                            caches.forEach(Cache::tick);
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            caches.forEach(Cache::tick);
        }

        public CacheBuilder<C> timeoutDelay(int timeoutAfter, TimeUnit timeUnit) {
            this.delTime = timeoutAfter;
            this.timeUnit = timeUnit;
            return this;
        }

        public CacheBuilder<C> onlyDeleteWhenUnused(boolean onlyDeleteWhenUnused) {
            this.unusedDelete = onlyDeleteWhenUnused;
            return this;
        }

        public CacheBuilder<C> deleteTooOldIndex(boolean deleteTooOldIndex, int deleteAfterIndex) {
            this.oldestIndexDelete = deleteTooOldIndex;
            this.maxIndex = deleteAfterIndex;
            return this;
        }

        public CacheBuilder<C> deleteAfterTimeout(boolean deleteAfterTimeout) {
            this.timeoutDelete = deleteAfterTimeout;
            return this;
        }

        public Cache<C> build() {
            Logger.log(this.getClass(), "Initialized new cache");
            Cache<C> cache = new Cache<>(delTime, timeUnit, unusedDelete, oldestIndexDelete, maxIndex, timeoutDelete);
            if (!running) {
                tick();
            }
            caches.add(cache);
            return cache;
        }
    }

    public static class CachedObject<T> {
        private long lastUsed;
        private final long added;
        private final T object;

        public CachedObject(T object) {
            this.object = object;
            this.lastUsed = System.currentTimeMillis();
            this.added = System.currentTimeMillis();
        }

        public long getLastUsed() {
            return lastUsed;
        }

        public long getAdded() {
            return added;
        }

        public T getObject() {
            lastUsed = System.currentTimeMillis();
            return object;
        }
    }
}
