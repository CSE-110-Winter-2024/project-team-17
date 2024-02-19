package edu.ucsd.cse110.successorator.date.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.Flashcard;

@Entity(tableName = "flashcards")
public class FlashcardEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;
    @ColumnInfo(name = "front")
    public String front;
    @ColumnInfo(name = "back")
    public String back;
    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    FlashcardEntity(@NonNull String front, @NonNull String back, int sortOrder) {
        this.front = front;
        this.back = back;
        this.sortOrder = sortOrder;
    }
    public static FlashcardEntity fromFlashcard(@NonNull Flashcard flashcard) {
        var card = new FlashcardEntity(flashcard.front(), flashcard.back(), flashcard.sortOrder());
        card.id = flashcard.id();
        return card;
    }
    public @NonNull Flashcard toFlashcard() {
        return new Flashcard(id, front, back, sortOrder);
    }
}