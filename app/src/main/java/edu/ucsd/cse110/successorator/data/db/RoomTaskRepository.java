package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
public class RoomTaskRepository implements TaskRepository{

    private final TaskDao taskDao;

    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Subject<Task> find(int id) {
        var entityLiveData = taskDao.findAsLiveData(id);
        var flashcardLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(flashcardLiveData);
    }

    @Override
    public void removeFinished() {
        taskDao.removeFinished();
    }

    @Override
    public Subject<List<Task>> findAll() {
        var entitiesLiveData = taskDao.findAllAsLiveData();
        var flashcardsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(flashcardsLiveData);
    }

    @Override
    public void save(Task task) {
        taskDao.insert(TaskEntity.fromTask(task));

    }

    @Override
    public void updateTask(int id, Task task) {
        remove(id);
        append(task);
    }

    @Override
    public void save(List<Task> flashcards) {
        var entities = flashcards.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    @Override
    public void remove(int id) {
        taskDao.delete(id);
    }

    @Override
    public void append(Task flashcard) {
        taskDao.append(TaskEntity.fromTask(flashcard));
    }

    @Override
    public void prepend(Task flashcard) {
        taskDao.prepend(TaskEntity.fromTask(flashcard));

    }

    @Override
    public void add(Task flashcard) {
        taskDao.add(TaskEntity.fromTask(flashcard));

    }

}
