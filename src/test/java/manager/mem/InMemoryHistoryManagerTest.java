package manager.mem;

import manager.HistoryManager;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    private Epic epic = new Epic("Test addNewTask", "Test addNewTask description", 1, 8, LocalDateTime.of(2023, 9, 8, 00, 00, 00));
    private Task task = new Task("Test addNewTask", "Test addNewTask description", 2, StatusTask.NEW, 8, LocalDateTime.of(2023, 1, 8, 00, 00, 00));
    private HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    @DisplayName("тест на пустую историю")
    void HistoryNull() {
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История пустая.");
    }

    @Test
    @DisplayName("тест штатный")
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    @DisplayName("тест дублирование")
    void addDoubling() {
        for (int i = 0; i < 2; i++) {
            historyManager.add(task);
        }
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "В истории больше элементов");
    }

    @Test
    @DisplayName("тест на удаление")
    void remove() {
        historyManager.add(epic);
        for (int i = 0; i < 2; i++) {
            historyManager.add(task);
        }
        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(), 4, 12, LocalDateTime.of(2023, 11, 8, 00, 00, 00));
        historyManager.add(subTask);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(2, historyManager.getHistory().size(), "История не пустая.");

        //Удалили элемент из середы
        historyManager.remove(task.getId());
        assertEquals(1, historyManager.getHistory().size(), "Удалили элемента из середы");
        //Удалили последний элемент
        historyManager.remove(subTask.getId());
        assertEquals(0, historyManager.getHistory().size(), "Удалили последний элемент");
        //удаление первого элемента
        historyManager.remove(epic.getId());
        assertEquals(0, historyManager.getHistory().size(), "Удалили все элементы");

    }
}