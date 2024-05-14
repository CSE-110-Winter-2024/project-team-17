package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AddTaskplusNameTest {

    @Test
    public void FinishedTask(){
        //only one task
        Task first = new Task(null, "Laundry", -1);
        Task second = new Task(null, "Clean", -1);

        List<Task> taskList = new ArrayList<>();
        taskList.add(first);
        first.flipFinished();
        assertTrue(first.finished());

        //2 task
        assertFalse(second.finished());
    }
    @Test
    public void threeMoreTaskChange(){
        Task first = new Task(1, "first", 0);
        Task second = new Task(2, "second", 1);
        Task third = new Task(2, "third", 2);
        third.flipFinished();
        List<Task> taskList = new ArrayList<>();
        taskList.add(first);
        taskList.add(second);
        taskList.add(third);
        first.flipFinished();
        taskList = Tasks.reorder(taskList, first);
        assertEquals(taskList.get(0).equals(second), true);
        assertEquals(taskList.get(1).equals(first), true);
        assertEquals(taskList.get(2).equals(third), true);
        second.flipFinished();
        third.flipFinished();
        taskList = Tasks.reorder(taskList, third);
        assertEquals(taskList.get(0).equals(third), true);
        assertEquals(taskList.get(1).equals(second), true);
        assertEquals(taskList.get(2).equals(first), true);
        second.flipFinished();
        taskList = Tasks.reorder(taskList, second);
        assertEquals(taskList.get(0).equals(third), true);
        assertEquals(taskList.get(1).equals(second), true);
        assertEquals(taskList.get(2).equals(first), true);
    }
}
