package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

//import org.testng.annotations.Test;

import java.util.Calendar;
import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.data.DateInMemoryDataSource;

public class DateTest {
    @Test
    public void testTimeFormat(){
        //testing correct time display and at 12AM without update.
        DateInMemoryDataSource source = DateInMemoryDataSource.fromDefault();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE ,1);
        calendar.set(Calendar.MONTH ,0);
        calendar.set(Calendar.HOUR, 1);
        source.setDate(calendar);
        assertEquals("Sunday, 12/31",source.getDate());

        //testing correct date change at 2AM
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE ,1);
        calendar.set(Calendar.MONTH ,0);
        calendar.set(Calendar.HOUR, 14);
        source.setDate(calendar);
        assertEquals("Monday, 01/01",source.getDate());

    }

}
