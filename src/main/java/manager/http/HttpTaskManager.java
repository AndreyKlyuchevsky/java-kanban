    package manager.http;

    import com.google.gson.Gson;
    import manager.file.FileBackedTasksManager;
    import manager.client.KVTaskClient;

    public class HttpTaskManager extends FileBackedTasksManager {
        private KVTaskClient client;

        public HttpTaskManager(String serverUrl) {
            super(serverUrl);
            // Инициализация клиента KVTaskClient с указанным URL сервера
            this.client = new KVTaskClient(serverUrl);
        }

        public static HttpTaskManager createWithInitialState(String serverUrl) {
            KVTaskClient client = new KVTaskClient(serverUrl);

            // Загрузка исходного состояния менеджера с сервера
            String initialState = client.load("task_manager_state");

            // Парсинг JSON и создание экземпляра менеджера
            Gson gson = new Gson();
            HttpTaskManager manager = gson.fromJson(initialState, HttpTaskManager.class);

            // Устанавливаем клиент
            manager.client = client;

            return manager;
        }

        @Override
        public void save() {
            // Конвертируем текущее состояние менеджера в JSON
            Gson gson = new Gson();
            String managerStateJson = gson.toJson(this);

            // Отправляем состояние менеджера на сервер KVServer
            client.put("task_manager_state", managerStateJson);
        }


        public KVTaskClient getClient() {
            return client;
        }
    }
