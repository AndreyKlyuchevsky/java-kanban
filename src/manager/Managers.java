package manager;

import manager.file.FileBackedTasksManager;

import java.io.File;

public class Managers {

    public TaskManager getDefault() {

        return  FileBackedTasksManager.loadFromFile(new File("C:/Users/andre/Desktop/Praktikum/java-kanban/filewriter.csv"));
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
