package manager.mem;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import manager.server.handler.NotFoundException;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected Map<Integer, Task> taskMap = new HashMap<>();
    protected Map<Integer, Epic> epicMap = new HashMap<>();
    protected Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private final HistoryManager history = new Managers().getDefaultHistory();
    protected final TreeSet<Task> taskTreeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int getNextId() {
        return id++;
    }

    protected int getId() {
        return id;
    }

    protected void setId(int Id){
        this.id=id;
    }

    @Override
    public void addTask(Task task) {
        validateTaskDurationInterval(task);
        task.setId(getNextId());
        taskMap.put(task.getId(), task);
        taskTreeSet.remove(task);
        taskTreeSet.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getNextId());
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subtask) {
        validateTaskDurationInterval(subtask);
        subtask.setId(getNextId());
        Epic epic  = epicMap.get(subtask.getEpicId());
        epic.addSubtaskId(subtask);
        subTaskMap.put(subtask.getId(), subtask);
        updateStatus(epic);
        taskTreeSet.add(subtask);
    }

    private void validateTaskDurationInterval(Task task) {
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (prioritizedTask.getId() == task.getId()) {
                continue;
            }
            if (isDateTimeBetween(task, prioritizedTask)) {
                throw new IllegalArgumentException("Invalid task duration interval: " + task);
            }

        }
    }

    private boolean isDateTimeBetween(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !(end1.isBefore(start2) || start1.isAfter(end2));

    }


    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(taskTreeSet);
    }

    public void updateStatus(Epic epic) {
        List<Integer> idSubtask = epic.getSubtaskIds();
        int countNew = 0;
        int countDone = 0;

        if (idSubtask.size() == 0) {
            epic.setStatus(StatusTask.NEW);
        } else {
            for (Integer integer : idSubtask) {
                SubTask subtasks = subTaskMap.get(integer);
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
        if (task == null) {
            throw new IllegalArgumentException("Задача должна быть заполнена");
        } else if (!taskMap.containsKey(task.getId())) {
            throw new NotFoundException("Задача не найдена");
        }
        taskTreeSet.remove(taskMap.get(task.getId()));
        validateTaskDurationInterval(task);
        taskMap.put(task.getId(), task);
        taskTreeSet.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Задача должна быть заполнена");
        } else if (!epicMap.containsKey(epic.getId())) {
            throw new NotFoundException("Задача не найдена");
        }
        Epic updateEpics = epicMap.get(epic.getId());
        updateEpics.setName(epic.getName());
        updateEpics.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        if (subtask == null) {
            throw new IllegalArgumentException("Задача должна быть заполнена");
        } else if (!subTaskMap.containsKey(subtask.getId())) {
            throw new NotFoundException("Задача не найдена");
        }
        validateTaskDurationInterval(subtask);
        taskTreeSet.remove(subTaskMap.get(subtask.getId()));
        subTaskMap.put(subtask.getId(), subtask);
        taskTreeSet.add(subtask);
        if (subtask.getEpicId() > 0) {
            Epic epic = epicMap.get(subtask.getEpicId());
            updateStatus(epic);
            epic.recalculate();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = taskMap.get(id);
        if (task != null) {
            history.add(task);}
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            history.add(epic);
        }
        return epic;

    }

    @Override
    public SubTask getSubtaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        if (subTask != null) {
            history.add(subTask);
        }
        return subTask;
    }

    @Override
    public List<SubTask> getSubTaskEpic(Epic epic) {
        List<SubTask> ListSubTask = new ArrayList<>();
        if (epicMap.containsKey(epic.getId())) {
            List<Integer> SubTaskId = epicMap.get(epic.getId()).getSubtaskIds();
            for (Integer integer : SubTaskId) {
                if (subTaskMap.containsKey(integer)) {
                    ListSubTask.add(subTaskMap.get(integer));
                }
            }
        }
        return ListSubTask;
    }

    @Override
    public List<Task> getTaskAll() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getEpicAll() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<SubTask> getSubTaskAll() {
        return new ArrayList<>(subTaskMap.values());
    }


    public List<Task> getAll() {
        ArrayList<Task> listTask = new ArrayList<>(taskMap.values());
        for (Map.Entry<Integer, Epic> integerEpicEntry : epicMap.entrySet()) {
            listTask.add(integerEpicEntry.getValue());
        }
        for (Map.Entry<Integer, SubTask> integerSubtaskEntry : subTaskMap.entrySet()) {
            listTask.add(integerSubtaskEntry.getValue());
        }
        return listTask;
    }


    @Override
    public void removeTaskAll() {
        for (Integer value : taskMap.keySet()) {
            history.remove(value);
            taskTreeSet.remove(taskMap.get(value));
        }
        taskMap.clear();
    }

    @Override
    public void removeEpicAll() {
        removeSubTaskAll();
        for (Integer value : epicMap.keySet()) {
            epicMap.remove(value);
        }
        epicMap.clear();
    }

    @Override
    public void removeSubTaskAll() {
        for (Integer value : subTaskMap.keySet()) {
            taskTreeSet.remove(subTaskMap.get(value));
            history.remove(value);
        }
        subTaskMap.clear();
        for (Epic value : epicMap.values()) {
            value.removeSubtaskAll();
            value.setStatus(StatusTask.NEW);
            value.setDuration(0);
            value.setStartTime(null);
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (taskMap.containsKey(id)) {
            taskTreeSet.remove(taskMap.get(id));
            taskMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTaskMap.containsKey(id)) {
            int idEpic = subTaskMap.get(id).getEpicId();
            Epic epic = epicMap.get(idEpic);
            epic.removeSubtaskId(id);
            taskTreeSet.remove(subTaskMap.get(id));
            subTaskMap.remove(id);
            history.remove(id);
            updateStatus(epic);
        }
    }


    @Override
    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {

            for (Integer subtaskId : epicMap.get(id).getSubtaskIds()) {
                subTaskMap.remove(subtaskId);
                history.remove(subtaskId);
            }
            epicMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

}
