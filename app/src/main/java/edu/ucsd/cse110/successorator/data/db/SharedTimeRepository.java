package edu.ucsd.cse110.successorator.data.db;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class SharedTimeRepository implements TimeKeeper {
    private DateSharedPref mySharedPreferences;

    public SharedTimeRepository(DateSharedPref sharedPreferences) {
        this.mySharedPreferences = sharedPreferences;
    }

    @Override
    public Subject<LocalDateTime> getDateTime() {
        // Wrap the LiveData from MySharedPreferences into a Subject
        return new LiveDataSubjectAdapter<>(mySharedPreferences.getDateTimeLiveData());
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        // Save the DateTime using MySharedPreferences
        mySharedPreferences.saveLocalDateTime("myDateTime", dateTime);
    }
}
