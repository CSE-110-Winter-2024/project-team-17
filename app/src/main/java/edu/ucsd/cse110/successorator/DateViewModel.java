package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class DateViewModel extends ViewModel {

    public static final ViewModelInitializer<DateViewModel> initializer =
            new ViewModelInitializer<>(
                    DateViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new DateViewModel(app.getDateRepository());

                    });


    private SimpleSubject<String> currentDateAndDay;
    private DateRepository dateRepository;


    public DateViewModel(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
        this.currentDateAndDay = new SimpleSubject<>();

        //initializing
        updateDate();

        dateRepository.findDateSubject().observe(date -> {
            if(date == null) return;

            currentDateAndDay.setValue(date);
        });
    }

    public SimpleSubject<String> getCurrentDateAndDay() {
        updateDate();
        return currentDateAndDay;
    }

    public void updateDate() {
        String updatedData = dateRepository.getDate();
        currentDateAndDay.setValue(updatedData);
    }

    public void advanceDate() {
        String updatedData = dateRepository.advanceDate();
        currentDateAndDay.setValue(updatedData);
        updateDate();
    }
}