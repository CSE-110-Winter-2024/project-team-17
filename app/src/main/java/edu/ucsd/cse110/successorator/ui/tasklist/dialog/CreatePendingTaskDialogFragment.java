package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import java.time.LocalDateTime; // Import the LocalDateTime class from java.time package
import java.time.DayOfWeek; // Import the DayOfWeek enum from java.time package
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreatePendingBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
public class CreatePendingTaskDialogFragment extends DialogFragment{

    private FragmentDialogCreatePendingBinding view;

    private MainViewModel activityModel;

    CreatePendingTaskDialogFragment() {

    }

    public static CreatePendingTaskDialogFragment newInstance() {
        var fragment = new CreatePendingTaskDialogFragment();
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
        this.view = FragmentDialogCreatePendingBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please provide the new task name.")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::OnNegativeButtonClick)
                .create();
    };

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var front = view.cardFrontEditText.getText().toString();
        LocalDateTime currentDateTime = LocalDateTime.now();

        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonthValue(); // Month value is 1-based
        int dayOfMonth = currentDateTime.getDayOfMonth();
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
        //TODO: Add task context
        String dateString = Integer.toString(dayOfWeekValue) + monthStr + dayStr + Integer.toString(year);
        var card = new Task(null, front, -1, false, dateString, 0, '\0');
        int frequency = -1;
        card.setFrequency(frequency);
        activityModel.add(card);
        dialog.dismiss();
    }

    private void OnNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

}
