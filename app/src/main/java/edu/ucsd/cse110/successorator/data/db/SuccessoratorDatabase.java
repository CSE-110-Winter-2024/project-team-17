package edu.ucsd.cse110.successorator.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TaskEntity.class}, version = 2)
public abstract class SuccessoratorDatabase extends  RoomDatabase{

    public abstract TaskDao flashcardDao();
}
