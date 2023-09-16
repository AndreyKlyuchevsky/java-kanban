package manager;

import manager.client.TaskClient;

public class FakeTaskClient implements TaskClient {
    @Override
    public String register() {
        // Заглушка для метода register, возвращает фейковый идентификатор
        return "fake_task_id";
    }

    @Override
    public void put(String key, String json) {
        // Заглушка для метода put, ничего не делает
    }

    @Override
    public String load(String key) {
        // Заглушка для метода load, всегда возвращает фейковый JSON
        return "{\"id\":\"fake_task_id\",\"title\":\"Fake Task\",\"description\":\"Fake Description\",\"status\":\"NEW\",\"estimate\":5}";
    }
}