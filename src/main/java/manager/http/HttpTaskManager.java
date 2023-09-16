    package manager.http;

    import com.google.gson.Gson;
    import manager.client.TaskClient;
    import manager.file.FileBackedTasksManager;
    import manager.client.KVTaskClient;

    public class HttpTaskManager extends FileBackedTasksManager {
        private final TaskClient client;

        public HttpTaskManager(String serverUrl) {
            super(serverUrl);
            // Инициализация клиента KVTaskClient с указанным URL сервера
            this.client = new KVTaskClient(serverUrl);
        }

        public HttpTaskManager(TaskClient client) {
            super("");
            // Инициализация клиента KVTaskClient с указанным URL сервера
            this.client = client;
        }

        @Override
        public void save() {
            // Конвертируем текущее состояние менеджера в JSON
            Gson gson = new Gson();
            String managerStateJson = gson.toJson(this);

            // Отправляем состояние менеджера на сервер KVServer
            client.put("task_manager_state", managerStateJson);
        }


        public TaskClient getClient() {
            return client;
        }
    }
