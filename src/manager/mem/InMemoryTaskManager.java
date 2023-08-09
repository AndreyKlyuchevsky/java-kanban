package manager.mem;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected final Map<Integer, Task> taskList = new HashMap<>();
    protected final Map<Integer, Epic> epicList = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskList = new HashMap<>();
    private final HistoryManager history = new Managers().getDefaultHistory();






    private int getId() {
        return id++;
    }

    @Override
    public void addTask(Task task) {
        task.setId(getId());
        taskList.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getId());
        epicList.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(Subtask subtask) {
        subtask.setId(getId());
        epicList.get(subtask.getEpicId()).setSubtaskId(subtask.getId());
        subtaskList.put(subtask.getId(), subtask);
        updateStatus(subtask.getEpicId());

    }


    private void updateStatus(int EpicId) {
        Epic epic = epicList.get(EpicId);
        List<Integer> idSubtask = epic.getSubtaskId();
        int countNew = 0;
        int countDone = 0;

        if (idSubtask.size() == 0) {
            epic.setStatus(StatusTask.NEW);
        } else {
            for (Integer integer : idSubtask) {
                Subtask subtasks = subtaskList.get(integer);
                if (subtasks.getStatus() == StatusTask.NEW) {
                    countNew++;
                } else if (subtasks.getStatus() == StatusTask.DONE) {
                    countDone++;
                }
            }
            if (countNew == idSubtask.size()) {
                epic.setStatus(StatusTask.NEW);
            } else if (countDone == idSubtask.size()) {
                epic.setStatus(StatusTask.DONE);
            } else {
                epic.setStatus(StatusTask.IN_PROGRESS);
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic updateEpics = epicList.get(epic.getId());
        updateEpics.setName(epic.getName());
        updateEpics.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        Subtask subtaskUpdate = subtaskList.get(subtask.getId());
        subtaskUpdate.setName(subtask.getName());
        subtaskUpdate.setDescription(subtask.getDescription());
        subtaskUpdate.setStatus(subtask.getStatus());
        updateStatus(subtask.getEpicId());
    }

    @Override
    public Task getTaskById(int id) {
        if (taskList.containsKey(id)) {
            history.add(taskList.get(id));
            return taskList.get(id);
        } else if (epicList.containsKey(id)) {
            history.add(epicList.get(id));
            return epicList.get(id);
        } else
            history.add(subtaskList.get(id));
        return subtaskList.getOrDefault(id, null);
    }

    @Override
    public ArrayList<Subtask> getSubTaskEpic(Epic epic) {
        ArrayList<Subtask> ListSubTask = new ArrayList<>();
        if (epicList.containsKey(epic.getId())) {
            ArrayList<Integer> SubTaskId = epicList.get(epic.getId()).getSubtaskId();
            for (Integer integer : SubTaskId) {
                if (subtaskList.containsKey(integer)) {
                    ListSubTask.add(subtaskList.get(integer));
                }
            }
        }
        return ListSubTask;
    }

    @Override
    public ArrayList<Task> getTaskAll() {

        return new ArrayList<>(taskList.values());
    }

    @Override
    public ArrayList<Epic> getEpicAll() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public ArrayList<Subtask> getSubTaskAll() {
        return new ArrayList<>(subtaskList.values());
    }


    public ArrayList<Task> getAll() {
        ArrayList<Task> listTask = new ArrayList<>(taskList.values());
        for (Map.Entry<Integer, Epic> integerEpicEntry : epicList.entrySet()) {
            listTask.add(integerEpicEntry.getValue());
        }
        for (Map.Entry<Integer, Subtask> integerSubtaskEntry : subtaskList.entrySet()) {
            listTask.add(integerSubtaskEntry.getValue());
        }
        return listTask;
    }

    @Override
    public void removeAll() {
        taskList.clear();
        epicList.clear();
        subtaskList.clear();
        history.removeAll();
    }

    @Override
    public void removeTaskAll() {
        for (Integer value : taskList.keySet()) {
            history.remove(value);
        }
        taskList.clear();
    }

    @Override
    public void removeEpicAll() {
        removeSubTaskAll();
        for (Integer value : epicList.keySet()) {
            epicList.remove(value);
        }
        epicList.clear();
    }

    @Override
    public void removeSubTaskAll() {
        for (Integer value : subtaskList.keySet()) {
            history.remove(value);
        }
        subtaskList.clear();
        for (Epic value : epicList.values()) {
            value.removeSubtaskAll();
            updateStatus(value.getId());
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            history.remove(id);
        } else if (epicList.containsKey(id)) {

            for (Integer subtaskId : epicList.get(id).getSubtaskId()) {
                subtaskList.remove(subtaskId);
                history.remove(subtaskId);
            }
            epicList.remove(id);
            history.remove(id);
        } else if (subtaskList.containsKey(id)) {
            int idEpic = subtaskList.get(id).getEpicId();
            epicList.get(idEpic).removeSubtaskId(id);
            subtaskList.remove(id);
            history.remove(id);

            updateStatus(idEpic);
        }
    }

    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) history.getHistory();
    }

}
