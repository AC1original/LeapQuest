package utils.caching;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.GameLoop;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class Cache<T> {
    private final LinkedHashSet<CachedObject<T>> cached = new LinkedHashSet<>();
    private final List<CacheListener> registeredClasses = Collections.synchronizedList(new ArrayList<>());
    private final boolean expires;
    private final int expiresAfter;
    private final TimeUnit expireTimeUnit;
    private final boolean deleteAfterExpiration;
    private final boolean onlyExpireWhenUnused;
    private final boolean deleteOldIndexes;
    private final int deleteIndexAfter;

    private Cache(final boolean expires, final int expiresAfter, final TimeUnit expireTimeUnit, final boolean deleteAfterExpiration,
                  final boolean onlyExpireWhenUnused, final boolean deleteOldIndexes, final int deleteIndexAfter) {
        this.expires = expires;
        this.expiresAfter = expiresAfter;
        this.expireTimeUnit = expireTimeUnit;
        this.deleteAfterExpiration = deleteAfterExpiration;
        this.onlyExpireWhenUnused = onlyExpireWhenUnused;
        this.deleteOldIndexes = deleteOldIndexes;
        this.deleteIndexAfter = deleteIndexAfter;
    }

    public void register(CacheListener listener) {
        registeredClasses.add(listener);
    }

    public void unregister(CacheListener listener) {
        registeredClasses.remove(listener);
    }

    private void tick() {
        if (isExpires()) {
           stream().filter(obj -> !obj.isExpired())
                   .filter(obj -> (System.currentTimeMillis() - (isOnlyExpireWhenUnused() ? obj.getLastUpdated() : obj.getTimeAdded())) >= getExpireTimeUnit().toMillis(getExpiresAfter()))
                   .forEach(obj -> {
                        registeredClasses.forEach(listener -> listener.onCachedObjectExpire(obj));
                       obj.expire();
                   });
        }
        if (isDeleteAfterExpiration()) {
            stream().filter(CachedObject::isExpired)
                    .findAny()
                    .ifPresent(this::remove);
        }

        if (isDeleteOldIndexes()) {
            if (cached.size() > getDeleteIndexAfter()) {
                remove(cached.getLast());
            }
        }


    }

    public void add(String key, T object) {
        CachedObject<T> cachedObject = new CachedObject<>(key, object);
        registeredClasses.forEach(listener -> listener.onCachedObjectAdd(cachedObject));
        synchronized (cached) {
            cached.add(cachedObject);
        }
    }

    @Nullable
    public T get(String key) {
        synchronized (cached) {
            Optional<CachedObject<T>> retu = stream()
                    .filter(obj -> obj.getKey().equals(key))
                    .findFirst();
            return retu.map(CachedObject::getObject).orElse(null);
        }
    }

    @Nullable
    public String getKey(T object) {
        synchronized (cached) {
            Optional<CachedObject<T>> retu = stream()
                    .filter(obj -> obj.object.equals(object))
                    .findFirst();
            return retu.map(CachedObject::getKey).orElse(null);
        }
    }

    public boolean contains(T object) {
        return getKey(object) != null;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public boolean contains(@NotNull CachedObject<?> cObject) {
        return contains(cObject.getKey());
    }

    public void remove(String key) {
        synchronized (cached) {
            cached.stream()
                    .filter(obj -> obj.getKey().equals(key))
                    .forEach(obj -> {
                        registeredClasses.forEach(listener -> listener.onCachedObjectRemove(obj));
                        cached.remove(obj);
                    });
        }
    }


    public void remove(T object) {
        synchronized (cached) {
            cached.stream()
                    .filter(obj -> obj.object.equals(object))
                    .forEach(obj -> remove(obj.getKey()));
        }
    }

    public void remove(CachedObject<?> cObject) {
        synchronized (cached) {
            registeredClasses.forEach(listener -> listener.onCachedObjectRemove(cObject));
            cached.remove(cObject);
        }
    }

    public Set<CachedObject<T>> getCopyOfCached() {
        return new LinkedHashSet<>(cached);
    }

    public Stream<CachedObject<T>> stream() {
        synchronized (cached) {
            return cached.stream();
        }
    }

    public void forEach(final Consumer<? super CachedObject<T>> action) {
        synchronized (cached) {
            cached.forEach(action);
        }
    }

    public boolean isExpires() {
        return expires;
    }

    public int getExpiresAfter() {
        return expiresAfter;
    }

    public TimeUnit getExpireTimeUnit() {
        return expireTimeUnit;
    }

    public boolean isDeleteAfterExpiration() {
        return deleteAfterExpiration;
    }

    public boolean isOnlyExpireWhenUnused() {
        return onlyExpireWhenUnused;
    }

    public boolean isDeleteOldIndexes() {
        return deleteOldIndexes;
    }

    public int getDeleteIndexAfter() {
        return deleteIndexAfter;
    }


    public static class CachedObject<T> {
        private final String key;
        private final T object;
        private final long timeAdded;
        private long lastUpdated;
        private boolean expired;

        private CachedObject(final String key, final T object) {
            this.key = key;
            this.object = object;
            this.timeAdded = System.currentTimeMillis();
            this.lastUpdated = timeAdded;
        }

        public String getKey() {
            return key;
        }

        public T getObject() {
            this.lastUpdated = System.currentTimeMillis();
            return object;
        }

        public long getTimeAdded() {
            return timeAdded;
        }

        public long getLastUpdated() {
            return lastUpdated;
        }

        public boolean isExpired() {
            return expired;
        }

        public void expire() {
            expired = true;
        }

        @Override
        public String toString() {
            return "CachedObject{" +
                    "key='" + key + '\'' +
                    ", object=" + object +
                    ", timeAdded=" + timeAdded +
                    ", lastUpdated=" + lastUpdated +
                    ", expired=" + expired +
                    '}';
        }
    }

    public static class CacheBuilder<T> {
        private static final Set<Cache<?>> caches = Collections.synchronizedSet(new HashSet<>());
        private boolean expires = false;
        private int expiresAfter = 1;
        private TimeUnit expireTimeUnit = TimeUnit.MINUTES;
        private boolean deleteAfterExpiration = false;
        private boolean onlyExpireWhenUnused = false;
        private boolean deleteOldIndexes = false;
        private int deleteIndexAfter = 100;

        static {
            tick();
        }

        private static void tick() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                new GameLoop().start(20, (_) -> {
                    caches.forEach(Cache::tick);
                });
            });
        }

        public CacheBuilder<T> objectsExpires(final boolean expires) {
            this.expires = expires;
            return this;
        }

        public CacheBuilder<T> objectsExpiresAfter(final int expiresAfter, final TimeUnit expireTimeUnit) {
            this.expiresAfter = expiresAfter;
            this.expireTimeUnit = expireTimeUnit;
            return this;
        }

        public CacheBuilder<T> deleteObjectsWhenExpired(final boolean deleteAfterExpiration) {
            this.deleteAfterExpiration = deleteAfterExpiration;
            return this;
        }

        public CacheBuilder<T> objectsOnlyExpiresWhenUnused(final boolean onlyExpireWhenUnused) {
            this.onlyExpireWhenUnused = onlyExpireWhenUnused;
            return this;
        }

        public CacheBuilder<T> deleteOldIndexes(final boolean deleteOldIndexes) {
            this.deleteOldIndexes = deleteOldIndexes;
            return this;
        }

        public CacheBuilder<T> deleteIndexAfter(final int deleteIndexAfter) {
            this.deleteIndexAfter = deleteIndexAfter;
            return this;
        }

        public final Cache<T> build() {
            Cache<T> cache = new Cache<T>(expires, expiresAfter, expireTimeUnit, deleteAfterExpiration, onlyExpireWhenUnused, deleteOldIndexes, deleteIndexAfter);
            caches.add(cache);
            return cache;
        }
    }
}
