package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.Serializable;
import java.util.Objects;


public class Task {

    private final @Nullable Integer id;
    private int sortOrder;
    private final String taskName;
    private boolean finished = false;

    private String addedDate;
    private String currOccurDate;
    private String nextOccurDate;
    private int frequency;

    private char tag;


    public Task (Integer id, String taskName, int sortOrder) {
        this.id = id;
        this.taskName = taskName;
        this.sortOrder = sortOrder;
    }

    public Task (Integer id, String taskName, int sortOrder, boolean finished, String addedDate, int frequency, char tag) {
        this.id = id;
        this.taskName = taskName;
        this.sortOrder = sortOrder;
        this.finished = finished;
        this.addedDate = addedDate;
        this.frequency = frequency;
        this.tag = tag;
        this.currOccurDate = new String(addedDate);
        this.nextOccurDate = null;
    }

    public @Nullable Integer id() {
        return id;
    }

    public @NonNull String taskName() {
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

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Task withId(int id) {
        return new Task(id, this.taskName, this.sortOrder, this.finished, this.addedDate, this.frequency, this.tag);
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(this.id, this.taskName, sortOrder, this.finished, this.addedDate, this.frequency, this.tag);
    }

    public Task withFinished(boolean finished) {
        return new Task(this.id, this.taskName, this.sortOrder, finished, this.addedDate, this.frequency, this.tag);
    }

    public void updateRecurrence() {
        //Can call this to update currOccurDate and nextOccurDate?
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

    public String addedDate() {
        return this.addedDate;
    }

    public String currOccurDate() { return this.currOccurDate;}

    public String nextOccurDate() { return this.nextOccurDate;}

    public int frequency() {
        return this.frequency;
    }

    public char tag() {
        return this.tag;
    }
}
