package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.data.db.DateSharedPref;
import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SharedTimeRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;

    private SharedTimeRepository dateRepo;




    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                SuccessoratorDatabase.class,
                "successorator-database"
        ).allowMainThreadQueries().build();
        DateSharedPref dateSharedPreference = new DateSharedPref(this);

        this.taskRepository = new RoomTaskRepository(database.flashcardDao());
        this.dateRepo = new SharedTimeRepository(dateSharedPreference);


        var sharedPreference = getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreference.getBoolean("isFirstRun", true);

        if(isFirstRun && database.flashcardDao().count() == 0){
            taskRepository.save(InMemoryDataSource.DEFAULT_CARDS);
            dateRepo.setDateTime(LocalDateTime.now());

            sharedPreference.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }

    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    public TimeKeeper getTimeRepo() {
        return dateRepo;
    }
}
