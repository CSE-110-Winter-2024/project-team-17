package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.Serializable;
import java.util.Objects;


public class Task {

    private final @Nullable Integer id;
    private int sortOrder;
    private String taskName;

    private boolean finished = false;


    public Task (Integer id, String taskName, int sortOrder) {
        this.id = id;
        this.taskName = taskName;
        this.sortOrder = sortOrder;
    }

    public Task (Integer id, String taskName, int sortOrder, boolean finished) {
        this.id = id;
        this.taskName = taskName;
        this.sortOrder = sortOrder;
        this.finished = finished;
    }

    public @Nullable Integer id() {
        return id;
    }

    public @NonNull String taskNmae() {
        return taskName;
    }


    public int sortOrder() {
        return sortOrder;
    }

    public boolean finished() {
        return finished;
    }

    public void flipFinished() {
        finished = !finished;
    }

    public Task withId(int id) {
        return new Task(id, this.taskName, this.sortOrder, this.finished);
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(this.id, this.taskName, sortOrder, this.finished);
    }

    public Task withFinished(boolean finished) {
        return new Task(this.id, this.taskName, this.sortOrder, finished);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(taskName, task.taskName) &&
                sortOrder == task.sortOrder &&
                finished == task.finished;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, sortOrder);
    }

    public void setSortOrder(int i) {
        this.sortOrder = i;
    }

}
