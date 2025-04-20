package org.muchiri;

import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);
    private static final String EXIT_COMMAND = "exit";

    public static void main(String[] args) {
        Cache cache = new Cache(initiateCache());
        promptCommand(cache);
        scan.close();
    }

    private static int initiateCache() {
        while (true) {
            try {
                System.out.println("Enter cache size (integer): ");
                String input = scan.nextLine();

                if (input.equalsIgnoreCase(EXIT_COMMAND)) {
                    System.out.println("Goodbye!");
                    scan.close();
                    System.exit(0);
                }

                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
    }

    private static void promptCommand(Cache cache) {
        while (true) {
            System.out.println("Enter command");
            String input = scan.nextLine();

            if (input.equalsIgnoreCase(EXIT_COMMAND)) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                String[] prompt = validCommand(input);
                String result = executeCommand(cache, prompt);
                System.out.println(result);

            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    private static String executeCommand(Cache cache, String[] prompt) {
        return switch (prompt[0]) {
            case "get" -> cache.get(prompt[1]);
            case "put" -> cache.put(prompt[1], prompt[2]);
            case "print" -> cache.print();
            default -> throw new IllegalArgumentException("Invalid command");
        };
    }

    private static String[] validCommand(String command) {
        String[] splitCommand = command.split(" ");

        if (!splitCommand[0].equals("get") && !splitCommand[0].equals("put") && !splitCommand[0].equals("print")) {
            throw new IllegalArgumentException("Enter valid command! Command should start with either get, put or print");
        }

        if (splitCommand[0].equals("get") && splitCommand.length != 2) {
            throw new IllegalArgumentException("Enter valid command! Enter key after get command");
        }

        if (splitCommand[0].equals("put") && splitCommand.length != 3) {
            throw new IllegalArgumentException("Enter valid command! enter a key and a value after put command");
        }

        if (splitCommand[0].equals("print") && splitCommand.length > 1) {
            throw new IllegalArgumentException("Enter valid command! Enter no arguments after print command");
        }

        return splitCommand;
    }

}