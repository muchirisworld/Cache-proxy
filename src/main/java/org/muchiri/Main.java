package org.muchiri;

import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);
    private static final String EXIT_COMMAND = "exit";

    public static void main(String[] args) {
        promptCommand();
        scan.close();
    }

    private static void promptCommand() {
        while (true) {
            System.out.println("Enter command");
            String input = scan.nextLine();

            if (input.equalsIgnoreCase(EXIT_COMMAND)) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                String[] prompt = validCommand(input);
                String result = executeCommand(prompt);
                System.out.println(result);

            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    private static String executeCommand(String[] prompt) {
        return switch (prompt[0]) {
            case "get" -> Cache.get(prompt[1]);
            case "put" -> Cache.put(prompt[1], prompt[2]);
            default -> throw new IllegalArgumentException("Invalid command");
        };
    }

    private static String[] validCommand(String command) {
        String[] splitCommand = command.split(" ");

        if (!splitCommand[0].equals("get") && !splitCommand[0].equals("put")) {
            throw new IllegalArgumentException("Enter valid command! Command should start with either get or put");
        }

        if (splitCommand[0].equals("get") && splitCommand.length != 2) {
            throw new IllegalArgumentException("Enter valid command! Enter key after get command");
        }

        if (splitCommand[0].equals("put") && splitCommand.length != 3) {
            throw new IllegalArgumentException("Enter valid command! enter a key and a value after put command");
        }

        return splitCommand;
    }

}