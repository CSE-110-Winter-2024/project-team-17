package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.data.db.SharedTimeRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Tasks;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {

    private final MutableSubject<List<Task>> orderedCards;
    private final TaskRepository taskRepository;
    private final MutableSubject<LocalDateTime> time;
    private final TimeKeeper timeRepo;
    private int timeOffset = 1;
    private int timeAdvCnt = 0;
    private MutableLiveData<Integer> uiState = new MutableLiveData<>();
    private final MutableSubject<List<Task>> filteredTasks = new SimpleSubject<>();
    private final MutableLiveData<Character> contextFilter = new MutableLiveData<>();





    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository(), app.getTimeRepo());
                    });

    public MainViewModel(TaskRepository taskRepository, TimeKeeper timeRepo) {
        this.taskRepository = taskRepository;
        this.timeRepo = timeRepo;

        // Create the observable subjects.
        this.orderedCards = new SimpleSubject<>();
        this.time = new SimpleSubject<>();

        // US5 start
        // Set up an initial observer to repository subjects which transforms Subject to LiveData or another Subject
        Subject<List<Task>> allTasksSubject = taskRepository.findAll();
        allTasksSubject.observe(new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                // Trigger filtered tasks update whenever the task list changes
                updateFilteredTasks(tasks, contextFilter.getValue());
            }
        });

        // React to changes in contextFilter, LiveData within ViewModel does not need a LifecycleOwner
        this.contextFilter.observeForever(new androidx.lifecycle.Observer<Character>() {
            @Override
            public void onChanged(Character filter) {
                // Assuming the latest task list needs to be refetched or maintained locally for updates
                List<Task> currentTasks = allTasksSubject.getValue(); // Getting the latest list of tasks
                updateFilteredTasks(currentTasks, filter);
            }
        });
        // US5 end


        // When the list of cards changes (or is first loaded), reset the ordering.
        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream().sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());

            orderedCards.setValue(newOrderedCards);
        });
        timeRepo.getDateTime().observe(dates -> {
            if(dates == null) return;

            time.setValue(dates);
            //TODO: This is causing some random removing task issue when switch view
            //deleteFinished();

        });
    }

    LiveData<Integer> getUIState() {
        return uiState;
    }

    public void setUIState(int newState) {
        uiState.setValue(newState);
    }

    public Subject<List<Task>> getOrderedCards() {
        return orderedCards;
    }

    public Subject<LocalDateTime> getTime() {
        return time;
    }

    public void add(Task task) {
        var tasks = this.orderedCards.getValue();
        var newTasks = Tasks.addNewTask(tasks, task);
        taskRepository.save(newTasks);
    }

    public void deleteFinished() {
        var tasks = this.orderedCards.getValue();
        if(tasks == null){
            return;
        }
        for(int i = tasks.size()-1; i>= 0; i--){
            if(tasks.get(i).finished()) {
                delete(tasks.get(i));
            }
        }
    }


    public void updateRecurrence() {
        var tasks = this.orderedCards.getValue();
        if(tasks == null) {
            return;
        }
        for (int i = 0; i <tasks.size(); i++) {
            tasks.get(i).updateRecurrence();
        }
        var newTasks = new ArrayList<>(tasks);
        taskRepository.save(newTasks);
    }

    public void delete(Task task){

        taskRepository.remove(task.id());
    }



    public void removeFinished() {
        taskRepository.removeFinished();
    }

    public void reorder(Task task) {
        var tasks = this.orderedCards.getValue();
        var newTasks = Tasks.reorder(tasks, task);
        taskRepository.save(newTasks);
    }

    public void append(Task card) {
        taskRepository.append(card);
    }

    public void timeAdvance(){
        //time.setValue(time.getValue().plusDays(timeOffset));
        //timeRepo.setDateTime(time.getValue().plusDays(timeOffset));
        //timeRepo.setDateTime(time.getValue().plusDays(timeOffset));
        timeAdvCnt++;
        deleteFinished();
    }


    public LocalDateTime getOffSetTime(){
        return time.getValue();
    }

    public int getTimeAdvCnt(){
        return timeAdvCnt;
    }

    public void timeSet(LocalDateTime now) {
        //time.setValue(now);
        timeRepo.setDateTime(now);
        //TODO: THis is probably causing random removing finish
        //deleteFinished();
    }

    // Method to apply filtering and sorting
    private void updateFilteredTasks(List<Task> tasks, Character filter) {
        if (tasks == null) return;
        List<Task> filteredList;
        if (filter == null) {
            filteredList = new ArrayList<>(tasks);
        } else {
            filteredList = tasks.stream()
                    .filter(task -> task.tag() == filter)
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());
        }
        filteredTasks.setValue(filteredList);
    }


    // Method to apply filtering and sorting
    public Subject<List<Task>> getFilteredTasks() {
        return filteredTasks;
    }

    public MutableLiveData<Character> getContextFilter() {
        return contextFilter;
    }

    public void setContextFilter(Character filter) {
        this.contextFilter.setValue(filter);
    }

    public void setDateforTask (Task task,int one){

        String setter = " ";

        //if move to today
        LocalDateTime current;
        if (one == 1) {
            current = time.getValue();
        }
            //if move to tomorrow
        else if (one == 0) {
            current = time.getValue().plusDays(timeOffset);
        } else {
            current = time.getValue().plusDays(timeOffset + timeOffset);
        }
        String dayOfWeek = Integer.toString(current.getDayOfWeek().getValue());
        String date = Integer.toString(current.getDayOfMonth());
        String year = Integer.toString(current.getYear());
        String month = Integer.toString(current.getMonthValue());

        setter = dayOfWeek + month + date + year;
        task.setDate(setter);
        reorder(task);
    }


    public void test(Task t){
        System.out.println(t.currOccurDate());
    }

    public void deleteTaskRec(Task t, int freq){
        for(int i=0; i<freq;i++){
            setDateforTask(t,0);
        }
        reorder(t);
    }
}


