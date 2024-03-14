package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity task);

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    List<Long> insert(List<TaskEntity> tasks);

    @Query("SELECT * FROM tasks WHERE id -:id")
    TaskEntity find(int id);

    @Query("SELECT * FROM tasks ORDER BY sort_order")
    List<TaskEntity> findALL();

    @Query("SELECT * FROM tasks WHERE id =:id")
    LiveData<TaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM tasks ORDER BY sort_order")
    LiveData<List<TaskEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM tasks")
    int count();

    @Query("SELECT MIN(sort_order) FROM tasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM tasks")
    int getMaxSortOrder();

    @Query("DELETE FROM tasks WHERE finished = true")
    void removeFinished();

    @Query("UPDATE tasks SET sort_order = sort_order + :by " + "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrder(int from, int to, int by);

    @Transaction
    default  int append(TaskEntity task){
        var maxSortOrder = getMaxSortOrder();
        var newFlashcard = new TaskEntity(
                task.name, maxSortOrder +1, task.finished,
                task.addedDate, task.currOccurDate, task.nextOccurDate, task.frequency, task.tag);
        return Math.toIntExact(insert(newFlashcard));
    }

    @Transaction
    default int prepend(TaskEntity task){
        shiftSortOrder(getMinSortOrder(),getMaxSortOrder(), 1);
        var newFlashcard = new TaskEntity(
                task.name, getMinSortOrder()-1, task.finished,
                task.addedDate, task.currOccurDate, task.nextOccurDate, task.frequency, task.tag);
        return Math.toIntExact(insert(newFlashcard));
    }

    @Transaction
    default  int add(TaskEntity task){
        var newFlashcard = new TaskEntity(
                task.name, task.sortOrder, task.finished,
                task.addedDate, task.currOccurDate, task.nextOccurDate, task.frequency, task.tag);
        return Math.toIntExact(insert(newFlashcard));
    }

    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);
}
