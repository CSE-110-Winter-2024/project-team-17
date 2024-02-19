package edu.ucsd.cse110.successorator.date.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FlashcardEntity.class}, version = 1)
public abstract class SECardsDatabase extends RoomDatabase {
    public abstract FlashcardDao flashcardDao();
}