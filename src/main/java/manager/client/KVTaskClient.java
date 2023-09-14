package manager.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class KVTaskClient implements TaskClient {
    private final String serverUrl;
    private final String apiToken;

    public KVTaskClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.apiToken = register();
    }

    @Override
    public String register() {
        try {
            URL url = new URL(serverUrl + "/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Wrap the InputStream in a try-with-resources block to ensure it's properly closed
                try (InputStream responseStream = connection.getInputStream()) {
                    try (Scanner scanner = new Scanner(responseStream, "UTF-8")) {
                        return scanner.useDelimiter("\\A").next();
                    }
                }
            } else {
                throw new KVTaskClientException("Failed to register with server. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new KVTaskClientException("Failed to register with server", e);
        }
    }

    @Override
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
                throw new KVTaskClientException("Failed to save data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new KVTaskClientException("Failed to save data", e);
        }
    }

    @Override
    public String load(String key) {
        try {
            URL url = new URL(serverUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Получаем данные из ответа сервера
                try (InputStream responseStream = connection.getInputStream()) {
                    Scanner scanner = new Scanner(responseStream, "UTF-8");
                    String data = scanner.useDelimiter("\\A").next();
                    scanner.close();
                    return data;
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Если данные не найдены, вернем null
                return null;
            } else {
                throw new KVTaskClientException("Failed to load data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new KVTaskClientException("Failed to load data", e);
        }
    }

}
