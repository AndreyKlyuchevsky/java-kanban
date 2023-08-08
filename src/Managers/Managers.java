package Managers;

import java.io.File;

public class Managers {

    public TaskManager getDefault() {
        return new FileBackedTasksManager(new File("C:/Users/andre/Desktop/Praktikum/java-kanban/filewriter.csv"));
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
