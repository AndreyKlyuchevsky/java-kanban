package manager.client;


import java.util.HashMap;
import java.util.Map;

public class FakeTaskClient implements TaskClient {
    private Map<String,String> contentMap = new HashMap<>();
    @Override
    public String register() {
        // Заглушка для метода register, возвращает фейковый идентификатор
        return "fake_task_id";
    }

    @Override
    public void put(String key, String json) {
        // Заглушка для метода put, ничего не делает
        contentMap.put(key, json);
    }

    @Override
    public String load(String key) {
        // Заглушка для метода load, всегда возвращает фейковый JSON
        return contentMap.get(key);
    }
}