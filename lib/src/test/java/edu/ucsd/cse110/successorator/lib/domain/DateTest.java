package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

//import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import org.junit.Test;

public class DateTest {

    @Test
    public void testTimeSetters(){
        LocalDateTime now = LocalDateTime.of(2024, 03, 14, 12, 12, 12);
        //testing correct time display and at 12AM without update.
        assertEquals(now.getYear(), 2024);
        assertEquals(now.getMonth().getValue(), 3);
        assertEquals(now.getDayOfMonth(), 14);
        //Time manipulation
        now = now.plusDays(18);
        assertEquals(now.getYear(), 2024);
        assertEquals(now.getMonth().getValue(), 4);
        assertEquals(now.getDayOfMonth(), 1);


    }



}
