package com.ombremoon.tennocraft.cache;

import com.google.common.cache.Cache;

import java.util.HashMap;
import java.util.Map;

public class AttributeCache<K, V> {
    private Map<K, CacheObject<K, V>> map;
    private CacheObject<K, V> first, last;
    private int cacheSize;
    private final int CAPACITY;
    private int hitCount = 0;
    private int missCount = 0;

    public AttributeCache(int capacity) {
        this.CAPACITY = capacity;
        this.map = new HashMap<>(this.CAPACITY);
    }

    public void put(K key, V value) {
        CacheObject<K, V> cacheObject = new CacheObject<K, V>(key, value);

        if (!map.containsKey(key)) {
            if (getCacheSize() >= CAPACITY) {
                removeObject(first);
            }
            putObjectLast(cacheObject);
        }
        map.put(key, cacheObject);
    }

    public void removeObject(CacheObject cacheObject) {
        if(cacheObject == null) {
            return;
        }
        if (cacheObject == last) {
            last = cacheObject.getPrev();
        }
        if (cacheObject == first) {
            first = cacheObject.getNext();
        }
        map.remove(cacheObject.getKey());
        cacheSize--;
    }

    private void putObjectLast(CacheObject cacheObject) {
        if (last != null) {
            last.setNext(cacheObject);
            cacheObject.setPrev(last);
        }

        last = cacheObject;
        if (first == null) {
            first = cacheObject;
        }
        cacheSize++;
    }

    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        CacheObject cacheObject = (CacheObject) map.get(key);
        cacheObject.incrementCacheObjects();
        rearrangeCache(cacheObject);
        return (V) cacheObject.getValue();
    }

    private void rearrangeCache(CacheObject cacheObject) {
        if (cacheObject == last) {
            return;
        }
        if (cacheObject == first) {
            first = cacheObject.getNext();
        } else {
            cacheObject.getPrev().setNext(cacheObject.getNext());
        }
        last.setNext(cacheObject);
        cacheObject.setPrev(last);
        cacheObject.setNext(null);
        last = cacheObject;
    }

    public void delete(K key) {
        removeObject(map.get(key));
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    public int getHitCount() {
        return this.hitCount;
    }

    public int getMissCount() {
        return this.missCount;
    }
}
