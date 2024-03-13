package edu.ucsd.cse110.successorator;


import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.data.db.SharedTimeRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Tasks;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel{

    private final MutableSubject<List<Task>> orderedCards;

    private final TaskRepository taskRepository;

    private final MutableSubject<LocalDateTime> time;

    private final TimeKeeper timeRepo;

    private int timeOffset = 1;

    private int timeAdvCnt = 0;

    private MutableLiveData<Integer> uiState = new MutableLiveData<>();

    LiveData<Integer> getUIState() {
        return uiState;
    }

    public void setUIState(int newState) {
        uiState.setValue(newState);
    }

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
            if(tasks.get(i).finished()){
                taskRepository.remove(tasks.get(i).id());
            }else{
                break;
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
        time.setValue(time.getValue().plusDays(timeOffset));
        timeAdvCnt++;
        deleteFinished();
    }

    public LocalDateTime getOffSetTime(){
        return time.getValue().plusDays(timeAdvCnt);
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


}
