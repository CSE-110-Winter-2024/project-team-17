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

    @ColumnInfo(name = "addedDate")
    public String addedDate;

    @ColumnInfo(name = "currOccurDate")
    public String currOccurDate;

    @ColumnInfo(name = "nextOccurDate")
    public String nextOccurDate;

    @ColumnInfo(name = "frequency")
    public int frequency;

    @ColumnInfo(name = "tag")
    public char tag;


    public TaskEntity(String name, int sortOrder, boolean finished, String addedDate, String currOccurDate, String nextOccurDate, int frequency, char tag) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.finished = finished;
        this.addedDate = addedDate;
        this.frequency = frequency;
        this.tag = tag;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var card = new TaskEntity(task.taskName(), task.sortOrder(), task.finished(), task.addedDate(), task.currOccurDate(), task.nextOccurDate(), task.frequency(), task.tag());
        card.id = task.id();
        return card;
    }

    public @NonNull Task toTask() {
        return new Task(id, name, sortOrder, finished, addedDate, frequency, tag);
    }
}
