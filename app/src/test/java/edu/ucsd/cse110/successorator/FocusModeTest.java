package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.time.LocalDateTime;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

@RunWith(RobolectricTestRunner.class)
public class FocusModeTest {

    @Mock
    private TimeKeeper mockTimeKeeper;
    private MainViewModel model;
    private SimpleTaskRepository taskRepo;
    private InMemoryDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SimpleSubject<LocalDateTime> mockDateTimeSubject = new SimpleSubject<>();
        mockDateTimeSubject.setValue(LocalDateTime.now()); // Set some default value

        Mockito.when(mockTimeKeeper.getDateTime()).thenReturn(mockDateTimeSubject);

        dataSource = InMemoryDataSource.fromDefault();
        taskRepo = new SimpleTaskRepository(dataSource);
        model = new MainViewModel(taskRepo, mockTimeKeeper); // Use mocked TimeKeeper
    }

    /*
     * Given there is an H task and an S task
     * When the user taps the hamburger button
     * And the user chooses the S focus mode
     * Then the user sees only the tasks tagged with S
     */
    @Test
    public void chooseFocusModeTest() {
        // Setup
        Task hTask = new Task(1, "Home Task", 1, false, "", 0, 'H');
        Task sTask = new Task(2, "School Task", 2, false, "", 0, 'S');
        model.add(hTask);
        model.add(sTask);

        // Execute
        model.setContextFilter('S');

        // Verify
        List<Task> visibleTasks = model.getFilteredTasks().getValue();
        assertTrue("The list of tasks should only contain tasks tagged with 'S'",
                visibleTasks.stream().allMatch(task -> task.tag() == 'S'));
        assertEquals("There should be only one task visible in S focus mode", 1, visibleTasks.size());
        assertTrue("The visible task should be the S task", visibleTasks.contains(sTask));
    }

    /*
     * Given there is an H task and an S task
     * And the user is the S focus mode
     * When the user taps the hamburger button
     * And the user chooses to cancel focus mode
     * Then the user sees all the tasks
     */
    @Test
    public void cancelFocusModeTest() {
        // Setup
        Task hTask = new Task(3, "Home Cleaning", 3, false, "", 0, 'H');
        Task sTask = new Task(4, "Study Session", 4, false, "", 0, 'S');
        model.add(hTask);
        model.add(sTask);
        model.setContextFilter('S'); // Initially in S focus mode

        // Execute
        model.setContextFilter(null); // Cancel focus mode

        // Verify
        List<Task> visibleTasks = model.getFilteredTasks().getValue();
        assertNotNull("Visible tasks should not be null when focus mode is canceled", visibleTasks);
        assertEquals("There should be two tasks visible when focus mode is canceled", 2, visibleTasks.size());
        assertTrue("Visible tasks should contain the home task", visibleTasks.contains(hTask));
        assertTrue("Visible tasks should contain the school task", visibleTasks.contains(sTask));
    }
}
