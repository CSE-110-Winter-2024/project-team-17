package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

//import org.testng.annotations.Test;

import java.util.Calendar;
import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.data.DateInMemoryDataSource;

public class DateTest {
    //Test format and test update on 2AM
    @Test
    public void testTimeFormat(){
        //testing correct time display and at 12AM without update.
        DateInMemoryDataSource source = DateInMemoryDataSource.fromDefault();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE ,1);
        calendar.set(Calendar.MONTH ,0);
        calendar.set(Calendar.HOUR, 10);
        source.setDate(calendar);
        assertEquals("Monday, 01/01",source.getDate());


    }



}
