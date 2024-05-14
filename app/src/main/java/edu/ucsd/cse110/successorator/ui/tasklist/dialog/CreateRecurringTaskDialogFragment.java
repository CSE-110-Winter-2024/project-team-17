package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;

import java.time.LocalDateTime; // Import the LocalDateTime class from java.time package
import java.time.DayOfWeek; // Import the DayOfWeek enum from java.time package
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateRecurringBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class CreateRecurringTaskDialogFragment extends DialogFragment{

    private FragmentDialogCreateRecurringBinding view;

    private MainViewModel activityModel;
    private char selectedTag = 'H';
    private ImageView lastSelectedTag;

    CreateRecurringTaskDialogFragment() {

    }

    public static CreateRecurringTaskDialogFragment newInstance() {
        var fragment = new CreateRecurringTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateRecurringBinding.inflate(getLayoutInflater());
        setupTagSelection();

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::OnNegativeButtonClick)
                .create();
    };

    private void setupTagSelection() {
        // Initialize ImageViews from the binding
        ImageView homeTag = view.homeTag;
        ImageView workTag = view.workTag;
        ImageView schoolTag = view.schoolTag;
        ImageView errandTag = view.errandTag;

        // Set the default selected tag
        setSelectedTag(homeTag, 'H'); // Default to homeTag and 'H' for Home

        // Set click listeners and update the selected tag accordingly
        homeTag.setOnClickListener(v -> setSelectedTag((ImageView) v, 'H'));
        workTag.setOnClickListener(v -> setSelectedTag((ImageView) v, 'W'));
        schoolTag.setOnClickListener(v -> setSelectedTag((ImageView) v, 'S'));
        errandTag.setOnClickListener(v -> setSelectedTag((ImageView) v, 'E'));
    }

    private void setSelectedTag(ImageView selectedTag, char tag) {
        if (lastSelectedTag != null) {
            lastSelectedTag.setSelected(false); // Unselect the last ImageView
        }

        selectedTag.setSelected(true); // Select the new ImageView
        lastSelectedTag = selectedTag;

        this.selectedTag = tag; // Update the selected tag
    }

    //TODO: Add task context
    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var front = view.cardFrontEditText.getText().toString();
        LocalDateTime currentDateTime = LocalDateTime.now();

        int year = Integer.parseInt(view.editTextYear.getText().toString());
        int month = Integer.parseInt(view.editTextMonth.getText().toString()); // Month value is 1-based
        int dayOfMonth = Integer.parseInt(view.editTextDate.getText().toString());;
        LocalDate selectedDate = LocalDate.of(year, month, dayOfMonth);
        LocalDateTime dateTime = selectedDate.atTime(LocalTime.NOON);

        try {
            // Attempt to parse the input into a LocalDateTime object
            //dateTime = LocalDateTime.parse(date, formatter);
            // If parsing is successful, break out of the loop
        } catch (DateTimeParseException e) {
            // If parsing fails, inform the user and continue the loop
            System.out.println("Invalid date format. Please enter a valid date.");
            newInstance();
        }

        DayOfWeek dayOfWeek = currentDateTime.getDayOfWeek();
        // You can then use the DayOfWeek enum directly, or you can get its value as an int if needed
        int dayOfWeekValue = dayOfWeek.getValue(); // Monday is 1, Sunday is 7
        String dayOfWeekName = dayOfWeek.toString();

        int[] dateArray = new int[4];
        dateArray[0] = dayOfWeekValue;
        dateArray[1] = month;
        dateArray[2] = dayOfMonth;
        dateArray[3] = year;
        String monthStr;
        if (month < 10) {
            monthStr = "0" + Integer.toString(month);
        }
        else {
            monthStr = Integer.toString(month);
        }
        String dayStr;
        if (dayOfMonth < 10) {
            dayStr = "0" + Integer.toString(dayOfMonth);
        }
        else {
            dayStr = Integer.toString(dayOfMonth);
        }
        String dateString = Integer.toString(dayOfWeekValue) + monthStr + dayStr + Integer.toString(year);
        var card = new Task(null, front, -1, false, dateString, 0, selectedTag);
        int frequency = 0;
        if (view.dailyRadioButton.isChecked()) {
            frequency = 1;
        }
        else if (view.weeklyRadioButton.isChecked()) {
            frequency = 7;
        }
        else if (view.monthlyRadioButton.isChecked()) {
            double freq  = Math.ceil(dayOfMonth/7.0); //-Frequency is the number of weeks away from the first week
            // E.g. day of sunday, with frequency = -3, is the 3rd "sunday" of the month
            frequency = (int)(0-freq);
        }
        else if (view.yearlyRadioButton.isChecked()) {
            frequency = 365; //Frequency will be determined based on if its a leap year or not
            //Leap year we +1
        }
        card.setFrequency(frequency);
        activityModel.calculateRecurrence(card);
        activityModel.add(card);
        dialog.dismiss();
    }

    private void OnNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
