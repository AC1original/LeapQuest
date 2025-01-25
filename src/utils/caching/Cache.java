package utils.caching;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Cache<T> {
    private final Set<CachedObject<T>> cached = Collections.synchronizedSet(new LinkedHashSet<>());
    private final boolean expires;
    private final int expiresAfter;
    private final TimeUnit expireTimeUnit;
    private final boolean deleteAfterExpiration;
    private final boolean onlyExpireWhenUnused;
    private final boolean deleteOldIndexes;
    private final int deleteIndexAfter;

    protected Cache(final boolean expires, final int expiresAfter, final TimeUnit expireTimeUnit, final boolean deleteAfterExpiration,
                    final boolean onlyExpireWhenUnused, final boolean deleteOldIndexes, final int deleteIndexAfter) {
        this.expires = expires;
        this.expiresAfter = expiresAfter;
        this.expireTimeUnit = expireTimeUnit;
        this.deleteAfterExpiration = deleteAfterExpiration;
        this.onlyExpireWhenUnused = onlyExpireWhenUnused;
        this.deleteOldIndexes = deleteOldIndexes;
        this.deleteIndexAfter = deleteIndexAfter;
    }

    private void tick() {
        if (isExpires()) {
            stream().filter(obj -> (onlyExpireWhenUnused ? obj.lastUpdated : obj.timeAdded) > expireTimeUnit.toMillis(expiresAfter))
                    .forEach(obj -> obj.expired = true);
        }
        if (isDeleteAfterExpiration()) {
            cached.removeIf(CachedObject::isExpired);
        }

        if (isDeleteOldIndexes()) {
        }
    }

    public void add(String key, T object) {
        cached.add(new CachedObject<>(key, object));
    }

    @Nullable
    public T get(String key) {
        Optional<CachedObject<T>> retu = cached.stream()
                .filter(obj -> obj.getKey().equals(key))
                .findFirst();

        return retu.map(CachedObject::getObject).orElse(null);
    }

    @Nullable
    public String getKey(T object) {
        Optional<CachedObject<T>> retu = cached.stream()
                .filter(obj -> obj.object.equals(object))
                .findFirst();

        return retu.map(CachedObject::getKey).orElse(null);
    }

    public boolean contains(T object) {
        return getKey(object) == null;
    }

    public boolean contains(String key) {
        return get(key) == null;
    }

    public boolean contains(@NotNull CachedObject<?> cObject) {
        return contains(cObject.getKey());
    }

    public void remove(String key) {
        cached.removeIf(obj -> obj.getKey().equals(key));
    }

    public void remove(T object) {
        cached.removeIf(obj -> obj.object.equals(object));
    }

    public void remove(CachedObject<?> cObject) {
        cached.remove(cObject);
    }

    public void replace(String key, T newObject) {
        remove(key);
        add(key, newObject);
    }

    public Set<CachedObject<T>> getCopyOfCached() {
        return new LinkedHashSet<>(cached);
    }

    public Stream<CachedObject<T>> stream() {
        return cached.stream();
    }

    public void forEach(final Consumer<? super CachedObject<T>> action) {
        cached.forEach(action);
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
    }
}
