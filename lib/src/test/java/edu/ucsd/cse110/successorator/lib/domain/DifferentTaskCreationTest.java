package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDateTime;

public class DifferentTaskCreationTest {


    //US6 Testing BDD for each task creation. (Weekly)
    @Test
    public void testWeeklyTask() {
        LocalDateTime now = LocalDateTime.of(2024, 2, 27, 12, 12);
        Task weeklyTask = new Task(null, "Throw out garbage", 1,
                false, "2024/2/27", 7, 'h');
        assertEquals(weeklyTask.currOccurDate(), "2024/2/27");


    }

    @Test
    public void testDailyTask() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 12);
        Task weeklyTask = new Task(null, "Throw out garbage", 1,
                false, "2024/1/1", 1, 'h');
        assertEquals(weeklyTask.currOccurDate(), "2024/1/1");
    }

    @Test
    public void testYearlyTask() {
        LocalDateTime now = LocalDateTime.of(2024, 2, 29, 12, 12);
        Task weeklyTask = new Task(null, "Birthday", 1,
                false, "2024/2/29", 365, 'h');
        assertEquals(weeklyTask.currOccurDate(), "2024/2/29");
    }

    @Test
    public void testOneTimeTask() {
        LocalDateTime now = LocalDateTime.of(2024, 2, 29, 12, 12);
        Task weeklyTask = new Task(null, "Throw out garbage", 1,
                false, "2024/2/29", 0, 'h');
        assertEquals(weeklyTask.currOccurDate(), "2024/2/29");
        assertEquals(weeklyTask.nextOccurDate(), null);
    }
}
