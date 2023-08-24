package manager.Test;

import manager.file.FileBackedTasksManager;


import java.io.File;
import java.io.IOException;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final FileBackedTasksManager manager = new FileBackedTasksManager(new File("filewriter.csv"));

    FileBackedTasksManagerTest() throws IOException {
    }

    @Override
    protected FileBackedTasksManager getManager() {
        return manager;
    }


}