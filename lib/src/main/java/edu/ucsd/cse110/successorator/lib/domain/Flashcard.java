package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class Flashcard implements Serializable {
    private final @Nullable Integer id;
    private final @NonNull String front;
    private final @NonNull String back;

    private boolean finished = false;
    private int sortOrder;

    public Flashcard(@Nullable Integer id, @NonNull String front, @NonNull String back, int sortOrder) {
        this.id = id;
        this.front = front;
        this.back = back;
        this.sortOrder = sortOrder;
    }

    public @Nullable Integer id() {
        return id;
    }

    public @NonNull String front() {
        return front;
    }

    public @NonNull String back() {
        return back;
    }

    public int sortOrder() {
        return sortOrder;
    }

    public Flashcard withId(int id) {
        return new Flashcard(id, this.front, this.back, this.sortOrder);
    }

    public Flashcard withSortOrder(int sortOrder) {
        return new Flashcard(this.id, this.front, this.back, sortOrder);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flashcard flashcard = (Flashcard) o;
        return Objects.equals(id, flashcard.id) &&
                Objects.equals(front, flashcard.front) &&
                Objects.equals(back, flashcard.back) &&
                sortOrder == flashcard.sortOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, front, back, sortOrder);
    }

    public boolean finished() {
        return finished;
    }

    public void flipFinished(){
        finished = !finished;
    }
}
