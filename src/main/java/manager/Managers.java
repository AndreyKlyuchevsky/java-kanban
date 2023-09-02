package manager;

import manager.file.FileBackedTasksManager;
import manager.mem.InMemoryHistoryManager;

import java.io.File;

public class Managers {

    public TaskManager getDefault() {

        return new HttpTaskManager("http://localhost:8078");
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
