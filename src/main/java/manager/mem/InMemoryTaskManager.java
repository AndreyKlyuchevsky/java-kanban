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
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>();
    protected final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    private final HistoryManager history = new Managers().getDefaultHistory();
    private final TreeSet <Task> taskTreeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int getId() {
        return id++;
    }

    @Override
    public void addTask(Task task) {
        validateTaskDurationInterval(task);
        task.setId(getId());
        taskMap.put(task.getId(), task);
        resetTaskTreeSet();
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getId());
        epicMap.put(epic.getId(), epic);
        resetTaskTreeSet();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        validateTaskDurationInterval(subtask);
        subtask.setId(getId());
        epicMap.get(subtask.getEpicId()).addSubtaskId(subtask);
        subTaskMap.put(subtask.getId(), subtask);
        updateStatus(subtask.getEpicId());
        resetTaskTreeSet();
    }

    private void validateTaskDurationInterval(Task task) {
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if(prioritizedTask.getId()==task.getId()){
                continue;
            }
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
        return new ArrayList<>(taskTreeSet);
    }

    private void resetTaskTreeSet(){
        taskTreeSet.clear();
        taskTreeSet.addAll(getTaskAll());
    }


    public void updateStatus(int EpicId) {
        Epic epic = epicMap.get(EpicId);
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
        if (task==null){
            throw new IllegalArgumentException("Задача должна быть заполнена");
        }else if(!taskMap.containsKey(task.getId())){
            throw new IllegalArgumentException("Задача не найдена");
        }
        validateTaskDurationInterval(task);
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic==null){
            throw new IllegalArgumentException("Задача должна быть заполнена");
        }else if(!epicMap.containsKey(epic.getId())){
            throw new IllegalArgumentException("Задача не найдена");
        }
        Epic updateEpics = epicMap.get(epic.getId());
        updateEpics.setName(epic.getName());
        updateEpics.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        if (subtask==null){
            throw new IllegalArgumentException("Задача должна быть заполнена");
        }else if(!subTaskMap.containsKey(subtask.getId())){
            throw new IllegalArgumentException("Задача не найдена");
        }
        validateTaskDurationInterval(subtask);
        subTaskMap.put(subtask.getId(), subtask);
        updateStatus(subtask.getEpicId());
        if(subtask.getEpicId()>0){
            epicMap.get(subtask.getEpicId()).recalculate();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (taskMap.containsKey(id)) {
            Task task = taskMap.get(id);
            history.add(task);
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            history.add(epic);
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubtaskById(int id) {
        if (subTaskMap.containsKey(id)) {
            SubTask subTask = subTaskMap.get(id);
            history.add(subTask);
            return subTask;
        } else {
            return null;
        }
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
            history.remove(value);
        }
        subTaskMap.clear();
        for (Epic value : epicMap.values()) {
            value.removeSubtaskAll();
            updateStatus(value.getId());
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTaskMap.containsKey(id)) {
            int idEpic = subTaskMap.get(id).getEpicId();
            epicMap.get(idEpic).removeSubtaskId(id);
            subTaskMap.remove(id);
            history.remove(id);

            updateStatus(idEpic);
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