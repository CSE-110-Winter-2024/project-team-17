package edu.ucsd.cse110.successorator;

import android.app.Application;

import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.data.DateInMemoryDataSource;

public class SuccessoratorApplication extends Application {
    private DateInMemoryDataSource dateSource;
    private DateRepository dateRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        this.dateSource = DateInMemoryDataSource.fromDefault();
        this.dateRepo = new DateRepository(dateSource);
    }

    public DateRepository getDateRepository() {
        return dateRepo;
    }
}
