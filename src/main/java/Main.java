import com.google.gson.Gson;
import manager.file.FileBackedTasksManager;
import manager.server.HttpTaskServer;
import manager.server.KVServer;
import manager.server.KVTaskClient;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.testng.AssertJUnit.assertEquals;


public class Main {
    private static final String URL = "http://localhost:8080/tasks/task?id=1";

    public static void main(String[] args) throws IOException, InterruptedException {
        FileBackedTasksManager taskManagerOld = new FileBackedTasksManager("filewriter.csv");
        KVServer server = new KVServer();
        KVTaskClient client = new KVTaskClient(URL);

        server.start();

        // Ожидание для демонстрации работы сервера и клиента
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Сохраняем и загружаем данные с использованием разных ключей
        client.put("key1", "value1");
        client.put("key2", "value2");

        String value1 = client.load("key1");
        String value2 = client.load("key2");

        System.out.println("Value for key1: " + value1); // Должно быть "value1"
        System.out.println("Value for key2: " + value2); // Должно быть "value2"

        // Обновляем значение и проверяем
        client.put("key1", "new_value");

        String updatedValue1 = client.load("key1");

        System.out.println("Updated value for key1: " + updatedValue1); // Должно быть "new_value"

        server.stop();


    }
        @Test
        public void testGetTask() throws IOException {
            // Создаем URL для GET-запроса на получение задачи
            URL url = new URL("http://localhost:8080/tasks/task?id=1");

            // Открываем соединение
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Проверяем, что статус ответа - 200 OK
            assertEquals(200, connection.getResponseCode());

            // Здесь вы можете добавить дополнительные проверки для полученного JSON-ответа
            // Используйте Gson для разбора JSON, чтобы проверить, что данные верны




        }






}
