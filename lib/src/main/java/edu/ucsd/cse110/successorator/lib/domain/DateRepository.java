package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.data.DateInMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class DateRepository {
    private final Calendar calendar = null;
    private final DateInMemoryDataSource dateSource;

    public DateRepository(DateInMemoryDataSource dateSource) {
        this.dateSource = dateSource;
    }

    public SimpleSubject<String> findDateSubject(){
        return dateSource.getDateSubject();
    }

    public String getDate(){
        dateSource.setDate(calendar);
        return dateSource.getDate();
    }


    public String advanceDate() {
        dateSource.advanceDate(calendar);
        return dateSource.getDate();
    }
}