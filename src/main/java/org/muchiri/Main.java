package org.muchiri;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<String[]> arguments = parseArguments(args);
            List<String[]> validatedArgs = validate(arguments);

            int port = 4000;
            String origin = null;
            boolean clearCache = false;

            for (String[] arg : validatedArgs) {
                switch (arg[0]) {
                    case "--port" -> port = Integer.parseInt(arg[1]);
                    case "--origin" -> origin = arg[1];
                    case "--clear-cache" -> clearCache = true;
                }
            }

            if (clearCache) {
                Cache cache = new Cache(3);
                //TODO: Implement cache clearing
                System.exit(0);
            }

            Server server = new Server(port, origin);
            server.start();

        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static List<String[]> parseArguments(String[] args) {
        List<String[]> arguments = new ArrayList<>();

        if (args.length < 2) {
            arguments.add(args);
            return arguments;
        }

        for (int i = 0; i < args.length - 1; i += 2) {
            if (args[i].startsWith("--")) {
                arguments.add(new String[]{args[i], args[i + 1]});
            }
        }
        return arguments;
    }

    private static List<String[]> validate(List<String[]> args) {
        for (String[] pair : args) {
            String flag = pair[0];
            String value = pair.length > 1 ? pair[1] : null;

            if (!ArgType.isValid(flag)) {
                throw new IllegalArgumentException("Invalid argument: " + flag);
            }

            if (flag.equals("--port")) {
                validatePort(value);
            } else if (flag.equals("--origin")) {
                validateOrigin(value);
            }
        }
        return args;
    }

    private static void validatePort(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port number: " + value);
        }
    }

    private static void validateOrigin(String value) {
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            throw new IllegalArgumentException("Invalid origin format! Origin must start with 'http://' or 'https://'");
        }
    }
}
