package manager.mem;

import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager getManager() {
        return new InMemoryTaskManager();
    }
}