package manager.http;

import manager.client.FakeTaskClient;
import manager.TaskManagerTest;
import manager.client.TaskClient;


public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    protected TaskClient client = new FakeTaskClient();

    @Override
    public HttpTaskManager getManager() {
        return new HttpTaskManager(client);
    }
}
