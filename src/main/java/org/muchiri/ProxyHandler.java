package org.muchiri;

import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyHandler implements Runnable {
    private final Socket clientSocket;
    private final String origin;
    private final Cache cache;

    public ProxyHandler(Socket clientSocket, String origin, Cache cache) {
        this.clientSocket = clientSocket;
        this.origin = origin;
        this.cache = cache;
    }

    @Override
    public void run() {
        try (
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String requestLine = clientReader.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length != 3) return;

            String method = requestParts[0];
            String path = requestParts[1];

            while (!clientReader.readLine().isEmpty()) {}

            // Check cache first
            String cachedResponse = cache.get(path);
            if (!cachedResponse.equals("Key '" + path + "' not found")) {
                clientWriter.write("HTTP/1.1 200 OK\r\n");
                clientWriter.write("X-Cache: HIT\r\n");
                clientWriter.write("Content-Type: application/json\r\n");
                clientWriter.write("\r\n");
                clientWriter.write(cachedResponse);
                clientWriter.flush();
                return;
            }

            URL targetUrl = new URL(origin + path);
            HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod(method);
            conn.setRequestProperty("X-Cache", "MISS");

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            // Start building the response
            StringBuilder responseBody = new StringBuilder();
            try (BufferedReader originReader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = originReader.readLine()) != null) {
                    responseBody.append(line).append("\n");
                }
            }

            // Cache the response
            cache.put(path, responseBody.toString());

            // Send response to client
            clientWriter.write("HTTP/1.1 " + responseCode + " " + responseMessage + "\r\n");
            clientWriter.write("X-Cache: MISS\r\n");
            for (Map.Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
                if (header.getKey() != null) {
                    for (String value : header.getValue()) {
                        clientWriter.write(header.getKey() + ": " + value + "\r\n");
                    }
                }
            }
            clientWriter.write("\r\n");
            clientWriter.write(responseBody.toString());
            clientWriter.flush();

        } catch (IOException e) {
            System.err.println("Proxy error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }
}
