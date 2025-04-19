package org.muchiri;

import java.util.Arrays;

public class Cache {
    private static final LRUCache store = new LRUCache(3);

    private static boolean checkKey(String key) {
        return store.containsKey(key);
    }

    private static String formatString(String key, String value) {
        return "[" + key + "] -> [" + value + "]";
    }

    public static String put(String key, String value) {
        boolean existed = checkKey(key);
        store.put(key, value);

        if (existed) {
            return "Updated " + formatString(key, value);
        }
        return "Stored " + formatString(key, value);
    }

    public static String get(String key) {
        String value = store.get(key);

        if (value == null) return "Key '" + key + "' not found";
        return value;
    }

    public static String print() {
        String[] collect = store.entrySet().stream()
                .map(entry -> formatString(entry.getKey(), entry.getValue()))
                .toArray(String[]::new);

        return Arrays.toString(collect);
    }
}
