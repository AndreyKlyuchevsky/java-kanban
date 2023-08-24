package manager.Test;
import manager.mem.InMemoryTaskManager;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private InMemoryTaskManager manager= new InMemoryTaskManager();

    @Override
    protected InMemoryTaskManager getManager() {
        return manager;
    }

}