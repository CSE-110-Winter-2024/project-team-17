package edu.ucsd.cse110.successorator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListAdapter;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListFragment;

@RunWith(RobolectricTestRunner.class)
public class ContextTest {

    @Mock private TimeKeeper mockTimeKeeper;
    @Mock private SimpleTaskRepository taskRepo;
    @Mock private Subject<LocalDateTime> mockDateTimeSubject;
    @Mock private MainViewModel mockViewModel;
    @Mock private TaskListAdapter adapter;
    @Mock private TaskListFragment fragment;
    @Mock private List<Task> initialTasks;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockTimeKeeper.getDateTime()).thenReturn(mockDateTimeSubject);
        Mockito.when(mockDateTimeSubject.getValue()).thenReturn(LocalDateTime.now()); // Provide a mock date time
    }

    @Test
    public void addContextsTest() {
        var dataSource = InMemoryDataSource.fromDefault();
        taskRepo = new SimpleTaskRepository(dataSource);
        var model = new MainViewModel(taskRepo, mockTimeKeeper); // Use mocked TimeKeeper

        // Given a new task related to the "H" (home) context
        String name = "Check Mail";
        char tag = 'H';
        Task newTask = new Task(0, name, 0, false, "", 0, tag);

        // When the task is added to the data source
        model.add(newTask);

        // Then it should be retrievable and correctly tagged
        Task retrievedTask = dataSource.getTask(newTask.id());
        assertNotNull("The task should exist in the data source", retrievedTask);
        assertEquals("The task name should match the one that was added", name, retrievedTask.taskName());
        assertEquals("The task context should be 'H'", tag, retrievedTask.tag());

        // Also, verify the task is reflected in the model's observable data (filtered tasks)
        assertTrue("The model's filtered task list should contain the new task",
                model.getOrderedCards().getValue().contains(newTask));
    }

    @Test
    public void addMultipleContextsTest() {
        var dataSource = InMemoryDataSource.fromDefault();
        taskRepo = new SimpleTaskRepository(dataSource);
        var model = new MainViewModel(taskRepo, mockTimeKeeper); // Use mocked TimeKeeper

        // Given there are two existing tasks
        Task existingTask1 = new Task(1, "Old Task 1", 1, false, "", 0, 'W');
        Task existingTask2 = new Task(2, "Old Task 2", 2, false, "", 0, 'W');
        model.add(existingTask1);
        model.add(existingTask2);

        // And the user wants to add a new task related to the "W" (work) context
        String name = "Write Report";
        char tag = 'W';
        Task newTask = new Task(3, name, 3, false, "", 0, tag);

        // When the task is added through the model
        model.add(newTask);

        // Then the task should be retrievable and correctly tagged
        Task retrievedTask = taskRepo.find(newTask.id()).getValue();
        assertNotNull("The task should exist in the repository", retrievedTask);
        assertEquals("The task name should match the one that was added", name, retrievedTask.taskName());
        assertEquals("The task context should be 'W'", tag, retrievedTask.tag());

        // And the tasks are sorted in the correct order
        List<Task> tasks = model.getOrderedCards().getValue();
        assertNotNull("The task list should not be null", tasks);
        assertTrue("The task list should contain the new task", tasks.contains(newTask));
        // Verify the new task comes after the existing ones in the sorted list
        int newTaskIndex = tasks.indexOf(newTask);
        assertTrue("New task should come after existing tasks",
                tasks.indexOf(existingTask1) < newTaskIndex &&
                        tasks.indexOf(existingTask2) < newTaskIndex);
    }

    @Test
    public void finishContextTest() {
        // Given the initial state from setUp

        // When the first task is marked as finished
        Task finishedTask = initialTasks.get(0);
        finishedTask.flipFinished(); // Assuming flipFinished() toggles the finished state of the task
        adapter.notifyDataSetChanged(); // Simulate notifying the adapter of data change

        // Emulate ViewModel updating tasks based on business logic
        when(mockViewModel.getOrderedTasks()).thenReturn(initialTasks.stream()
                .sorted((task1, task2) -> Boolean.compare(task2.finished(), task1.finished())) // Move finished tasks to top
                .collect(Collectors.toList()));

        // Trigger any necessary updates (this would usually be done through LiveData observations)
        fragment.updateTasks(mockViewModel.getOrderedTasks().getValue());

        // Then the task is displayed as finished (with strike-through)
        assertTrue("First task should be marked as finished", finishedTask.finished());

        // And it is moved to the top of the list of finished tasks
        Task topTask = adapter.getItem(0); // Assuming the adapter reorders based on finished status
        assertEquals("Finished task should be at the top", finishedTask, topTask);

        // Additionally, check if the UI properties match expected finished task attributes
        // (This part is more challenging to test directly without an actual UI framework like Espresso)
    }

}
