package manager.http;

import com.google.gson.Gson;
import manager.client.TaskClient;
import manager.file.FileBackedTasksManager;
import manager.client.KVTaskClient;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public static class TaskManagerState {
        private Map<Integer, Task> taskMap = new HashMap<>();
        private  Map<Integer, Epic> epicMap = new HashMap<>();
        private  Map<Integer, SubTask> subTaskMap = new HashMap<>();

        public List<Integer> getHistory() {
            return history;
        }

        public void setHistory(List<Integer> history) {
            this.history = history;
        }

        private List<Integer> history = new ArrayList<>();

        public Map<Integer, Task> getTaskMap() {
            return taskMap;
        }

        public Map<Integer, Epic> getEpicMap() {
            return epicMap;
        }

        public Map<Integer, SubTask> getSubTaskMap() {
            return subTaskMap;
        }

        public void setTaskMap(Map<Integer, Task> taskMap) {
            this.taskMap = taskMap;
        }

        public void setEpicMap(Map<Integer, Epic> epicMap) {
            this.epicMap = epicMap;
        }

        public void setSubTaskMap(Map<Integer, SubTask> subTaskMap) {
            this.subTaskMap = subTaskMap;
        }
    }

}
