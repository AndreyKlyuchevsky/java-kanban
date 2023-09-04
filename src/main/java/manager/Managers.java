package manager;

import manager.http.HttpTaskManager;
import manager.mem.InMemoryHistoryManager;

public class Managers {

    public TaskManager getDefault() {

        return new HttpTaskManager("http://localhost:8078");
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
