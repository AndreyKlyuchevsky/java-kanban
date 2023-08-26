package manager.mem;

import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;




class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach // ревьюрер предлагал создать метод BeforeEach
    private void init(){
        manager = new InMemoryTaskManager();
    }
}