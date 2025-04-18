package org.muchiri;

import java.util.HashMap;

public class Cache {
    private static final HashMap<String, String> store = new HashMap<>();

    private static boolean checkKey(String key) {
        return store.containsKey(key);
    }

    public static String put(String key, String value) {
        boolean existed = checkKey(key);
        store.put(key, value);

        if (existed) {
            return "Updated [" + key + "] -> [" + value + "]";
        }
        return "Stored [" + key + "] -> [" + value + "]";
    }

    public static String get(String key) {
        String value = store.get(key);

        if (value == null) return "Key '" + key + "' not found";
        return value;
    }
}
