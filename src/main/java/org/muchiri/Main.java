package org.muchiri;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private final Cache cache;
    private final Socket socket;
    private final InputStreamReader reader;
    private final OutputStreamWriter writer;
    private final BufferedReader in;
    private final BufferedWriter out;

    public Main(ServerSocket serverSocket) throws IOException {
        this.cache = new Cache(3);
        this.socket = serverSocket.accept();
        this.reader = new InputStreamReader(socket.getInputStream());
        this.writer = new OutputStreamWriter(socket.getOutputStream());
        this.in = new BufferedReader(reader);
        this.out = new BufferedWriter(writer);
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4000);
        System.out.println("Server started on port 4000");
        
        while (true) {
            try {
                System.out.println("Waiting for client connection...");
                Main server = new Main(serverSocket);
                server.run();
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }
    }

    public void run() {
        try {
            out.write("Welcome to the cache server!");
            out.newLine();
            out.write("Available commands: get <key>, put <key> <value>, print, exit");
            out.newLine();
            out.flush();

            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Client requested to exit");
                    break;
                }
                
                try {
                    String[] prompt = validCommand(normalizeInput(input));
                    String result = executeCommand(cache, prompt);
                    out.write(result);
                    out.newLine();
                    out.flush();
                } catch (IllegalArgumentException e) {
                    out.write("Error: " + e.getMessage());
                    out.newLine();
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Error in client communication: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
            System.out.println("Closed connection with client");
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
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

    private static String normalizeInput(String input) {
        StringBuilder result = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch == '\b' || ch == 127) { // Handle backspace (BS or DEL)
                if (!result.isEmpty()) {
                    result.deleteCharAt(result.length() - 1);
                }
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
