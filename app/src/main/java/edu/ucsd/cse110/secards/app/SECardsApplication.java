package edu.ucsd.cse110.secards.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.secards.app.data.db.RoomFlashcardRepository;
import edu.ucsd.cse110.secards.app.data.db.SECardsDatabase;
import edu.ucsd.cse110.secards.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.secards.lib.domain.DateRepository;
import edu.ucsd.cse110.secards.lib.domain.FlashcardRepository;

public class SECardsApplication extends Application {
    private InMemoryDataSource dataSource;
    private FlashcardRepository flashcardRepository;
    private DateRepository dateRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();

        var database = Room.databaseBuilder(
                        getApplicationContext(), SECardsDatabase.class, "secards-database"
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
