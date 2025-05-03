package utils.caching;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.GameLoop;
import utils.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Stream;

//TODO: Fix CurrentModification Exception
public final class Cache<T> {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
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
            lock.writeLock().lock();
            try {
                stream().filter(CachedObject::isExpired)
                        .filter(obj -> (System.currentTimeMillis() - (isOnlyExpireWhenUnused() ? obj.getLastUpdated() : obj.getTimeAdded()))
                                >= getExpireTimeUnit().toMillis(getExpiresAfter()))
                        .forEach(obj -> {
                            registeredClasses.forEach(lstnr -> lstnr.onCachedObjectExpire(obj));
                            obj.expire();
                        });
            } finally {
                lock.writeLock().unlock();
            }
        }

        if (isDeleteAfterExpiration()) {
            lock.readLock().lock();
            try {
                stream().filter(CachedObject::isExpired)
                        .forEach(this::remove);
            } finally {
                lock.readLock().unlock();
            }
        }

        if (isDeleteOldIndexes() && cached.size() > getDeleteIndexAfter()) {
            remove(cached.getLast());
        }
    }


    public void add(String key, T object) {
        CachedObject<T> cachedObject = new CachedObject<>(key, object);
        lock.writeLock().lock();
        try {
            registeredClasses.forEach(listener -> listener.onCachedObjectAdd(cachedObject));
            cached.add(cachedObject);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Nullable
    public T get(String key) {
        lock.readLock().lock();
        try {
            return stream().filter(obj -> obj.getKey().equals(key))
                    .findFirst()
                    .map(CachedObject::getObject)
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Nullable
    public String getKey(T object) {
        lock.readLock().lock();
        try {
            return stream().filter(obj -> obj.object.equals(object))
                    .map(CachedObject::getKey)
                    .findFirst()
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
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
        lock.writeLock().lock();
        try {
            stream().filter(obj -> obj.getKey().equals(key))
                    .forEach(obj -> {
                        registeredClasses.forEach(lstnr -> lstnr.onCachedObjectRemove(obj));
                        cached.remove(obj);
                    });
        } finally {
            lock.writeLock().unlock();
        }
    }


    public void remove(T object) {
        lock.readLock().lock();
        try {
            stream().filter(obj -> obj.object.equals(object))
                    .forEach(obj -> remove(obj.getKey()));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove(CachedObject<?> cObject) {
        lock.writeLock().lock();
        try {
            registeredClasses.forEach(lstnr -> lstnr.onCachedObjectRemove(cObject));
            cached.remove(cObject);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Set<CachedObject<T>> getCopyOfCached() {
        return new LinkedHashSet<>(cached);
    }

    public Stream<CachedObject<T>> stream() {
        lock.readLock().lock();
        try {
            return cached.stream();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void forEach(final Consumer<? super CachedObject<T>> action) {
        lock.readLock().lock();
        try {
            cached.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            cached.clear();
        } finally {
            lock.writeLock().unlock();
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
            new GameLoop()
                    .runOnThread(true)
                    .runThreadVirtual(true)
                    .setThreadName("Caching-Thread")
                    .start(20, (_) -> {
                        caches.forEach(Cache::tick);
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
            Logger.info(cache, "Initialized new cache.");
            return cache;
        }
    }
}
