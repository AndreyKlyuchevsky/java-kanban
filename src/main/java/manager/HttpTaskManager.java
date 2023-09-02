package manager;


import com.google.gson.Gson;
import manager.file.FileBackedTasksManager;
import manager.server.KVTaskClient;
import model.Epic;
import model.SubTask;
import model.Task;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;

    public HttpTaskManager(String serverUrl) {
        super(serverUrl);
        // Инициализация клиента KVTaskClient с указанным URL сервера
        this.client = new KVTaskClient(serverUrl);

        // Загрузка исходного состояния менеджера с сервера
        String initialState = client.load("task_manager_state");


    }



}
