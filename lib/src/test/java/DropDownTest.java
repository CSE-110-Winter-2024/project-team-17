package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DropDownTest {
    //Testing US 6 move tomrrow button
    @Test
    public void testMoveTomrrow(){

        Task task = new Task(1, "Test",1);
        task.setDate("403142024");

        Task task2 = new Task(1, "Test2",1);
        task2.setDate("503152024");

        task.setDate("503152024");

        assertEquals(task.currOccurDate(), task2.currOccurDate());
    }

    //Testing US 6 move today button
    @Test
    public void testMoveToday(){

        Task task2 = new Task(1, "Test2",1);
        task2.setDate("503152024");

        Task task = new Task(1, "Test",1);
        task.setDate("403142024");

        task2.setDate("403142024");

        assertEquals(task.currOccurDate(), task2.currOccurDate());
    }


    //Testing US 6 Delete Task
    @Test
    public void testDeleteTask(){

        ArrayList<Task> testing = new ArrayList<Task>();

        Task task = new Task(1, "Test",1);
        task.setDate("403142024");

        testing.add(task);

        testing.remove(task);
        assertEquals(testing.size(), 0);
    }

    //Testing finish for US6
    @Test
    public void testMarkFinish(){
        Task task = new Task(1, "Test",1);
        task.flipFinished();

        assertTrue(task.finished());


    }
}
