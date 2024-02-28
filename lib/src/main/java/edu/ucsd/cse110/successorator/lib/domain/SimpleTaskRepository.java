package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleTaskRepository implements TaskRepository {
    private final InMemoryDataSource dataSource;

    public SimpleTaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Task> find(int id) {
        return dataSource.getFlashcardSubject(id);
    }

    @Override
    public Subject<List<Task>> findAll() {
        return dataSource.getAllTaskSubject();
    }

    @Override
    public void save(Task task) {
        dataSource.putTask(task);
    }

    @Override
    public void save(List<Task> tasks) {
        dataSource.putTasks(tasks);
    }

    @Override
    public void remove(int id) {
        dataSource.removeTask(id);
    }

    @Override
    public void append(Task task) {
        dataSource.putTask(
                task.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }


    @Override
    public void prepend(Task task) {
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putTask(
                task.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }

    @Override
    public void add(Task task) {
        dataSource.shiftSortOrders(task.sortOrder(), dataSource.getMaxSortOrder(), 1);
        dataSource.putTask(
                task.withSortOrder(task.sortOrder())
        );
    }

    @Override
    public void removeFinished() {

    }


}