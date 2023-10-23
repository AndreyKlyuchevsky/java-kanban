package manager;

import manager.http.HttpTaskManager;
import manager.mem.InMemoryHistoryManager;

public class Managers {

    public static TaskManager getDefault() {
       HttpTaskManager manager = new HttpTaskManager ("http://localhost:8078");
       HttpTaskManager.loadFromFile(manager);
       return  manager;
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
