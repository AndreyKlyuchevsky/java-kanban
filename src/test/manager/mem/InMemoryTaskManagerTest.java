package manager.mem;

import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;




class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach // ревьюрер предлагал создать метод BeforeEach
    protected void init(){
        manager = new InMemoryTaskManager();
        super.init();
    }
}