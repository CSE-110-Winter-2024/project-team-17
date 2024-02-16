package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Task {

    private final @Nullable Integer id;


    private final @NonNull String name;
    private boolean isFinished = false;
    private @NonNull int order;


    public Task(Integer id, String name, int order){
        this.id = id;
        this.name = name;
        this.order = order;
    }


    public @NonNull String name(){
        return this.name;
    }

    public int order(){
        return this.order;
    }

    public @NonNull Integer id(){
        return id;
    }

    public Task withId(Integer id) {
        return new Task(id, name, order);
    }

    public Task withOrder(int order) {
        return new Task(id, name, order);
    }

    public void setOrder(int order){
        this.order = order;
    }


    public void switchFinish(){
        this.isFinished = !isFinished;
    }

    public boolean finished() {
        return this.isFinished;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        //throw new NotImplementedException("hashCode");
        return Objects.hash(id, name);

    }


}
