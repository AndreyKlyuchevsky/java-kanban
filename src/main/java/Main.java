import manager.file.FileBackedTasksManager;
import manager.server.HttpTaskServer;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        FileBackedTasksManager taskManagerOld = new FileBackedTasksManager(new File("filewriter.csv"));
        HttpTaskServer server = new HttpTaskServer();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/hello");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    }
}
