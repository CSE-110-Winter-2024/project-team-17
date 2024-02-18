package edu.ucsd.cse110.secards.lib.domain;

import java.util.Calendar;

import edu.ucsd.cse110.secards.lib.data.DateInMemoryDataSource;
import edu.ucsd.cse110.secards.lib.util.SimpleSubject;

public class DateRepository {
    private Calendar calendar = Calendar.getInstance();
    private final DateInMemoryDataSource dateSource;

    public DateRepository(DateInMemoryDataSource dateSource) {
        this.dateSource = dateSource;
    }

    public SimpleSubject<String> findDateSubject(){
        return dateSource.getDateSubject();
    }

    public String getDate(){
        calendar = Calendar.getInstance();
        dateSource.setDate(calendar);
        return dateSource.getDate();
    }


    public String advanceDate() {
        dateSource.advanceDate(calendar);
        return dateSource.getDate();
    }
}