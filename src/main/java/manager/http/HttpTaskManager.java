package manager.http;

import com.google.gson.Gson;
import manager.client.TaskClient;
import manager.client.TaskManagerState;
import manager.file.FileBackedTasksManager;
import manager.client.KVTaskClient;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final TaskClient client;


    public HttpTaskManager(String serverUrl) {
        super("");
        this.client = new KVTaskClient(serverUrl);
    }

    public HttpTaskManager(TaskClient client) {
        super("");
        this.client = client;
    }

    @Override
    public void save() {
        // Конвертируем текущее состояние менеджера в JSON
        TaskManagerState taskManagerState = new TaskManagerState();
        taskManagerState.setTaskMap(this.taskMap);
        taskManagerState.setSubTaskMap(this.subTaskMap);
        taskManagerState.setEpicMap(this.epicMap);
        List<Integer> history = new ArrayList<>();

        for (Task tasks : super.getHistory()) {
            history.add(tasks.getId());
        }

        taskManagerState.setHistory(history);

        Gson gson = new Gson();
        String managerStateJson = gson.toJson(taskManagerState);

        // Отправляем состояние менеджера на сервер KVServer
        client.put("task_manager_state", managerStateJson);
    }

    @Override
    public void load() {
        // Загружаем JSON-состояние менеджера с сервера
        String managerStateJson = client.load("task_manager_state");

        if (managerStateJson != null) {
            // Если JSON-состояние получено, пытаемся преобразовать его обратно в объект TaskManagerState
            Gson gson = new Gson();
            TaskManagerState taskManagerState = gson.fromJson(managerStateJson, TaskManagerState.class);

            if (taskManagerState != null) {
                // Если преобразование удалось, устанавливаем состояние менеджера
                this.taskMap = taskManagerState.getTaskMap();
                this.subTaskMap = taskManagerState.getSubTaskMap();
                this.epicMap = taskManagerState.getEpicMap();
                this.taskMap.forEach((key, value) -> {
                    this.resetTaskTreeSet(value);
                });
                this.subTaskMap.forEach((key, value) -> {
                    this.resetTaskTreeSet(value);
                });
                // Восстанавливаем историю задач
                List<Integer> history = taskManagerState.getHistory();
                for (int taskId : history) {
                    this.getTaskById(taskId);
                }
            }
        }
    }

}