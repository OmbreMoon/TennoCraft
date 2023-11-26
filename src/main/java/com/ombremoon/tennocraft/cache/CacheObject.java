package com.ombremoon.tennocraft.cache;

public class CacheObject<K, V> {
    private K key;
    private V value;
    private int cacheObjects = 0;
    private CacheObject prev, next;

    public CacheObject(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void incrementCacheObjects() {
        this.cacheObjects++;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public CacheObject getNext() {
        return this.next;
    }

    public void setNext(CacheObject next) {
        this.next = next;
    }

    public CacheObject getPrev() {
        return this.prev;
    }

    public void setPrev(CacheObject prev) {
        this.prev = prev;
    }
}
