package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.Task;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;


    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "finished")
    public boolean finished;

    public TaskEntity(String name, int sortOrder, boolean finished) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.finished = finished;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var card = new TaskEntity(task.taskNmae(), task.sortOrder(), task.finished());
        card.id = task.id();
        return card;
    }

    public @NonNull Task toTask() {
        return new Task(id, name, sortOrder, finished);
    }
}
