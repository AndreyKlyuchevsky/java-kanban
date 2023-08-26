package manager.file;

import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach // ревьюрер предлагал создать метод BeforeEach
    private void init(){
        manager = new FileBackedTasksManager(new File("filewriter.csv"));
    }
}