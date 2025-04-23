package org.muchiri;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final Cache cache;
    private final ServerSocket serverSocket;
    private final String origin;

    public Server(Cache cache, int port, String origin) throws IOException {
        this.cache = cache;
        this.serverSocket = new ServerSocket(port);
        this.origin = origin;
    }

    public void start() {
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        while (true) {
            try {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new ProxyHandler(clientSocket, origin, cache));
                thread.start();
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }
    }
}
