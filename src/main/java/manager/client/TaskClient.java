package manager.client;

public interface TaskClient {
    String register();
    void put(String key, String json);
    String load(String key);
}