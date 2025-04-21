package org.muchiri;

public enum ArgType {
    PORT("--port"),
    ORIGIN("--origin"),
    CLEAR_CACHE("--clear-cache");

    private final String flag;

    ArgType(String flag) {
        this.flag = flag;
    }

    public static boolean isValid(String flag) {
        for (ArgType arg : values()) {
            if (arg.flag.equals(flag)) return true;
        }
        return false;
    }
}