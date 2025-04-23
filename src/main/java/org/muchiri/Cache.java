package org.muchiri;

import java.util.Arrays;

public class Cache {
    private final LRUCache store;

    public Cache(int capacity) {
        this.store = new LRUCache(capacity);;
    }

    private boolean checkKey(String key) {
        return store.containsKey(key);
    }

    private String formatString(String key, String value) {
        return "[" + key + "] -> [" + value + "]";
    }

    public String put(String key, String value) {
        boolean existed = checkKey(key);
        store.put(key, value);

        if (existed) {
            return "Updated " + formatString(key, value);
        }
        return "Stored " + formatString(key, value);
    }

    public String get(String key) {
        String value = store.get(key);

        if (value == null) return "Key '" + key + "' not found";
        return value;
    }

    public void clear() {
        store.clear();
        System.out.println("Cache cleared.");
    }


    public String print() {
        String[] collect = store.entrySet().stream()
                .map(entry -> formatString(entry.getKey(), entry.getValue()))
                .toArray(String[]::new);

        return Arrays.toString(collect);
    }
}
