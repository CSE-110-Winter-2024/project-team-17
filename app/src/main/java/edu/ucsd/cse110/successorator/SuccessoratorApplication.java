package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.date.db.RoomFlashcardRepository;
import edu.ucsd.cse110.successorator.date.db.TasksDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.domain.FlashcardRepository;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private FlashcardRepository flashcardRepository;
    private DateRepository dateRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();

        var database = Room.databaseBuilder(
                        getApplicationContext(), TasksDatabase.class, "secards-database"
                )
                .allowMainThreadQueries()
                .build();

        this.flashcardRepository = new RoomFlashcardRepository(database.flashcardDao());

        var sharedPreferences = getSharedPreferences("secards", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun && database.flashcardDao().count() == 0) {
            flashcardRepository.save(InMemoryDataSource.DEFAULT_CARDS);
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public FlashcardRepository getFlashcardRepository() {
        return flashcardRepository;
    }

    public DateRepository getDateRepository() { return dateRepository; }
}