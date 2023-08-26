package manager.mem;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected final Map<Integer, Task> taskList = new HashMap<>();
    protected final Map<Integer, Epic> epicList = new HashMap<>();
    protected final Map<Integer, SubTask> subtaskList = new HashMap<>();
    private final HistoryManager history = new Managers().getDefaultHistory();


    private int getId() {
        return id++;
    }

    @Override
    public void addTask(Task task) {
        validateTaskDurationInterval(task);
        task.setId(getId());
        taskList.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        validateTaskDurationInterval(epic);
        epic.setId(getId());
        epicList.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subtask) {
        validateTaskDurationInterval(subtask);
        subtask.setId(getId());
        epicList.get(subtask.getEpicId()).addSubtaskId(subtask);
        subtaskList.put(subtask.getId(), subtask);
        updateStatus(subtask.getEpicId());
    }

    private void validateTaskDurationInterval(Task task) {
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (isDateTimeBetween(task.getStartTime(), prioritizedTask.getStartTime(), prioritizedTask.getEndTime()) ||
                    isDateTimeBetween(task.getEndTime(), prioritizedTask.getStartTime(), prioritizedTask.getEndTime())) {
                throw new IllegalArgumentException("Invalid task duration interval: " + task);
            }
        }
    }


    private boolean isDateTimeBetween(LocalDateTime date, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (date.isEqual(startDateTime) || date.isAfter(startDateTime)) &&
                (date.isEqual(endDateTime) || date.isBefore(endDateTime));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        TreeSet <Task> taskTreeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        taskTreeSet.addAll(getTaskAll());
        return new ArrayList<>(taskTreeSet);
    }

    public void updateStatus(int EpicId) {
        Epic epic = epicList.get(EpicId);
        List<Integer> idSubtask = epic.getSubtaskIds();
        int countNew = 0;
        int countDone = 0;

        if (idSubtask.size() == 0) {
            epic.setStatus(StatusTask.NEW);
        } else {
            for (Integer integer : idSubtask) {
                SubTask subtasks = subtaskList.get(integer);
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
    public void updateSubTask(SubTask subtask) {
        SubTask subtaskUpdate = subtaskList.get(subtask.getId());
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
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicList.containsKey(id)) {
            history.add(epicList.get(id));
            return epicList.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubtaskById(int id) {
        if (subtaskList.containsKey(id)) {
            history.add(subtaskList.get(id));
            return subtaskList.getOrDefault(id, null);
        } else {
            return null;
        }
    }

    @Override
    public List<SubTask> getSubTaskEpic(Epic epic) {
        List<SubTask> ListSubTask = new ArrayList<>();
        if (epicList.containsKey(epic.getId())) {
            List<Integer> SubTaskId = epicList.get(epic.getId()).getSubtaskIds();
            for (Integer integer : SubTaskId) {
                if (subtaskList.containsKey(integer)) {
                    ListSubTask.add(subtaskList.get(integer));
                }
            }
        }
        return ListSubTask;
    }

    @Override
    public List<Task> getTaskAll() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<Epic> getEpicAll() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public List<SubTask> getSubTaskAll() {
        return new ArrayList<>(subtaskList.values());
    }


    public List<Task> getAll() {
        ArrayList<Task> listTask = new ArrayList<>(taskList.values());
        for (Map.Entry<Integer, Epic> integerEpicEntry : epicList.entrySet()) {
            listTask.add(integerEpicEntry.getValue());
        }
        for (Map.Entry<Integer, SubTask> integerSubtaskEntry : subtaskList.entrySet()) {
            listTask.add(integerSubtaskEntry.getValue());
        }
        return listTask;
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
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subtaskList.containsKey(id)) {
            int idEpic = subtaskList.get(id).getEpicId();
            epicList.get(idEpic).removeSubtaskId(id);
            subtaskList.remove(id);
            history.remove(id);

            updateStatus(idEpic);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epicList.containsKey(id)) {

            for (Integer subtaskId : epicList.get(id).getSubtaskIds()) {
                subtaskList.remove(subtaskId);
                history.remove(subtaskId);
            }
            epicList.remove(id);
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

}
