package manager;

import manager.file.FileBackedTasksManager;
import manager.mem.InMemoryHistoryManager;

import java.io.File;

public class Managers {

    public TaskManager getDefault() {

        return new FileBackedTasksManager(new File("filewriter.csv"));
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
