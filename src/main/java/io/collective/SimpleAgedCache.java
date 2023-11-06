package io.collective;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

public class SimpleAgedCache {
    private Clock clock;
    private Map<Object, Entry> cacheMap = initializeCacheMap();

    protected final Map<Object, Entry> initializeCacheMap() {
        return new HashMap<>();
    }

    public SimpleAgedCache(Clock clock) {
        this.clock = clock;
    }

    public SimpleAgedCache() {
        this.clock = Clock.system(Clock.systemDefaultZone().getZone());
    }

    public void put(Object key, Object value, int retentionInMillis) {
        long entryRetention = clock.millis() + retentionInMillis;
        cacheMap.put(key, new Entry(value, entryRetention));
    }

    public boolean isEmpty() {
        cleanCache();
        return cacheMap.isEmpty();
    }

    public int size() {
        cleanCache();
        return cacheMap.size();
    }

    public Object get(Object key) {
        cleanCache();
        Entry entry = cacheMap.get(key);
        if (entry != null){
            return entry.value;
        }
        return null;
    }

    private void cleanCache(){
        long currentTime = clock.millis();
        cacheMap.entrySet().removeIf(entry -> entry.getValue().Expired(currentTime));
    }

    private class Entry{
        Object value;
        long retentionInMillis;
        
        Entry(Object value, long retentionInMillis){
            this.value = value;
            this.retentionInMillis = retentionInMillis;
        }

        boolean Expired(long currentTime){
            return currentTime >= retentionInMillis;
        }
    }
}