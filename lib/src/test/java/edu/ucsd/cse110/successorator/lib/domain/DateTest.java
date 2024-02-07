package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

//import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.data.DateInMemoryDataSource;

public class DateTest {
    private final Calendar calendar = Calendar.getInstance();
    @Test
    public void testTimeFormat(){
        //testing correct time display and at 12AM without update.
        DateInMemoryDataSource source = DateInMemoryDataSource.fromDefault();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE ,1);
        calendar.set(Calendar.MONTH ,0);
        calendar.set(Calendar.HOUR, 1);
        source.setDate(calendar);
        assertEquals("Monday, 01/01",source.getDate());

        //testing correct date change at 2AM
        calendar.set(Calendar.DATE ,1);
        calendar.set(Calendar.MONTH ,0);
        calendar.set(Calendar.HOUR, 14);
        source.setDate(calendar);
        assertEquals("Tuesday, 01/02",source.getDate());

    }

}
