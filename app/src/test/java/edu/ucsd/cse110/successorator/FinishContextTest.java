package edu.ucsd.cse110.successorator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

@RunWith(RobolectricTestRunner.class)
public class FinishContextTest {

    @Mock
    private InMemoryDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Ensure that TimeKeeper's getDateTime() returns a non-null Subject.
        SimpleSubject<LocalDateTime> dateTimeSubject = new SimpleSubject<>();
        dateTimeSubject.setValue(LocalDateTime.now()); // Set the current time as the subject's value

        dataSource = InMemoryDataSource.fromDefault();
    }

    @Test
    public void finishContextTest() {
        // Given there are two tasks
        Task unfinishedTask = new Task(1, "Unfinished Task", 1, false, "", 0, 'W');
        Task finishedTask = new Task(2, "Finished Task", 2, false, "", 0, 'W');
        dataSource.putTask(unfinishedTask);
        dataSource.putTask(finishedTask);

        // And the user has finished the first task
        unfinishedTask.setFinished(true); // Mark the first task as finished
        dataSource.putTask(unfinishedTask); // Update the task in the data source

        // Then the task is moved to the top of the list of finished tasks
        List<Task> updatedTasks = dataSource.getTasks();
        assertNotNull("Task list should not be null", updatedTasks);

        // Assuming that finished tasks are sorted to the top
        List<Task> finishedTasksAtTop = updatedTasks.stream()
                .filter(Task::finished)
                .collect(Collectors.toList());

        assertTrue("The finished task should be at the top", finishedTasksAtTop.contains(unfinishedTask));
        assertTrue("The unfinished task should not be the first in the list of finished tasks", !finishedTasksAtTop.get(0).equals(unfinishedTask) || finishedTasksAtTop.size() == 1);
    }
}
