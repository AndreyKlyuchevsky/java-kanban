package manager.server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class KVTaskClient {
    private final String serverUrl;
    private final String apiToken;

    public KVTaskClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.apiToken = register();
    }

    private String register() {
        try {
            URL url = new URL(serverUrl + "/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Получаем токен из ответа сервера
                InputStream responseStream = connection.getInputStream();
                Scanner scanner = new Scanner(responseStream, "UTF-8");
                String token = scanner.useDelimiter("\\A").next();
                scanner.close();
                responseStream.close();
                return token;
            } else {
                throw new IOException("Failed to register with server. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to register with server", e);
        }
    }

    public void put(String key, String json) {
        try {
            URL url = new URL(serverUrl + "/save/" + key + "?API_TOKEN=" + apiToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = json.getBytes("UTF-8");
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to save data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data", e);
        }
    }

    public String load(String key) {
        try {
            URL url = new URL(serverUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Получаем данные из ответа сервера
                InputStream responseStream = connection.getInputStream();
                Scanner scanner = new Scanner(responseStream, "UTF-8");
                String data = scanner.useDelimiter("\\A").next();
                scanner.close();
                responseStream.close();
                return data;
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Если данные не найдены, вернем null
                return null;
            } else {
                throw new IOException("Failed to load data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data", e);
        }
    }
}
