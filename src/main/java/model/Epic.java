package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Epic extends Task {
    private List<SubTask> subTaskList = new ArrayList<>();
    private LocalDateTime endTime; // Рассчитываемое поле

    public Epic(String name, String description, int id, int duration, LocalDateTime startTime) {
        super(name, description, id, StatusTask.NEW, duration, startTime);
        type = TaskType.EPIC;
    }

    public Epic(String name, String description) {
        super(name, description, StatusTask.NEW);
        type = TaskType.EPIC;
    }

    public Epic(String name, String description, int id) {
        super(name, description, id, StatusTask.NEW);
        type = TaskType.EPIC;
    }

    public void addSubtaskId(SubTask subtask) {
        if (subtask != null && subtask.getId() > 0) {
            this.subTaskList.add(subtask);
            recalculate();
        }
    }

    public void recalculate() {
        startTime = subTaskList.stream()
                .min(Comparator.comparing(Task::getStartTime))
                .get()
                .getStartTime();
        endTime = subTaskList.stream()
                .max(Comparator.comparing(Task::getStartTime))
                .get()
                .getStartTime();

        duration = subTaskList.stream()
                .mapToInt(Task::getDuration).sum();
    }


    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskList, epic.subTaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskList);
    }

    public TaskType getType() {
        return type;
    }

    public void removeSubtaskId(int id) {
        for (int i = 0; i < subTaskList.size(); i++) {
            if (subTaskList.get(i).getId() != id) {
                break;
            }
            subTaskList.remove(i);
            recalculate();
        }

    }

    @Override
    public int getEpicId() {
        return getId(); // Return the ID of the epic
    }


    public void removeSubtaskAll() {
        subTaskList.clear();
    }

    public List<Integer> getSubtaskIds() {
        return subTaskList.stream()
                .map(SubTask::getId)
                .collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtaskId=" + subTaskList +
                '}' + "\n";
    }
}
