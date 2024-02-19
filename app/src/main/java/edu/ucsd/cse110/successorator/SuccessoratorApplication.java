package edu.ucsd.cse110.successorator;

import android.app.Application;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.data.DateInMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.FlashcardRepository;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private FlashcardRepository flashcardRepository;
    private DateRepository dateRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();
        this.flashcardRepository = new FlashcardRepository(dataSource);
    }

    public FlashcardRepository getFlashcardRepository() {
        return flashcardRepository;
    }

    public DateRepository getDateRepository() { return dateRepository; }
}
