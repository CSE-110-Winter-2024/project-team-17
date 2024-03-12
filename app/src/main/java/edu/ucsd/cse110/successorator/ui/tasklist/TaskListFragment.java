package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.TasksFragmentBinding;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;

public class TaskListFragment extends  Fragment{

    private MainViewModel activityModel;

    private TasksFragmentBinding view;
    private TaskListAdapter adapter;

    private Spinner spinner;

    //private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MM/dd");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd");
    private String formattedDateTime;
    private String formattedTmrDateTime;

    private Handler handler = new Handler();

    private String mode = "today";


    public TaskListFragment() {

    }

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter (with an empty list for now)
        //this.adapter = new CardListAdapter(requireContext(), List.of(), activityModel::remove);
        this.adapter = new TaskListAdapter(requireContext(), List.of(),activityModel /*, id -> {
            var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        }*/);
        activityModel.getOrderedCards().observe(cards -> {
            var newcards = cards;
            //Determine the current date as a string
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            int year = currentDateTime.getYear();
//            int month = currentDateTime.getMonthValue(); // Month value is 1-based
//            int dayOfMonth = currentDateTime.getDayOfMonth();
//            DayOfWeek dayOfWeek = currentDateTime.getDayOfWeek();
//            // You can then use the DayOfWeek enum directly, or you can get its value as an int if needed
//            int dayOfWeekValue = dayOfWeek.getValue(); // Monday is 1, Sunday is 7
//            String dayOfWeekName = dayOfWeek.toString();
//
//            int[] dateArray = new int[4];
//            dateArray[0] = dayOfWeekValue;
//            dateArray[1] = month;
//            dateArray[2] = dayOfMonth;
//            dateArray[3] = year;
//            String monthStr;
//            if (month < 10) {
//                monthStr = "0" + Integer.toString(month);
//            }
//            else {
//                monthStr = Integer.toString(month);
//            }
//            String dayStr;
//            if (dayOfMonth < 10) {
//                dayStr = "0" + Integer.toString(dayOfMonth);
//            }
//            else {
//                dayStr = Integer.toString(dayOfMonth);
//            }
//            String nowDate = Integer.toString(dayOfWeekValue) + monthStr + dayStr + Integer.toString(year);
//            for (int i = 0; i < newcards.size(); i++) {
//                //Extract the date from cards
//                String currDate = newcards.get(i).currOccurDate();
//                if (nowDate.compareTo(currDate) != 0) {
//                    newcards.remove(i);
//                }
//            }
            if (newcards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(newcards)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = TasksFragmentBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.listView.setAdapter(adapter);
        //view.dateView.setText("Test");

        view.AddButton.setOnClickListener(v -> {
            //TODO: Base on today list change the dialog
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });

        //TODO: Comment out these two lines


        /*if(LocalDateTime.now() != activityModel.getTime().getValue()){
            //activityModel.removeFinished();
            activityModel.timeSet(LocalDateTime.now());
        }*/
        String[] item = {activityModel.getTime().getValue().format(formatter),
                //TODO: When creating item must use the getOffsetTime due to Advance time
                activityModel.getOffSetTime().plusDays(1).format(formatter), "Pending","Recurring"};

        if(LocalDateTime.now() != activityModel.getTime().getValue()){
            //activityModel.removeFinished();
            activityModel.timeSet(LocalDateTime.now());
            String[] newItem = {activityModel.getTime().getValue().format(formatter),
                    activityModel.getOffSetTime().plusDays(1).format(formatter), "Pending","Recurring"};
            item = newItem;
        }
        updateTime();


        view.advanceButton.setOnClickListener(v -> {
            //TODO: Reset the right swap list view
            activityModel.timeAdvance();
            //activityModel.setUIState(1);
        });

        String[] daysItems = {"Today "+formattedDateTime, "Tmr "+formattedTmrDateTime, "Pending", "Recurring"};
        spinner = view.spinner;

        ArrayAdapter<String> daysadapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, daysItems);

        daysadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(daysadapter);

        //newly Added
        activityModel.getTime().observe(dates -> {
            if( dates == null) return;
            formattedDateTime = activityModel.getTime().getValue().format(formatter);
            formattedTmrDateTime = activityModel.getTime().getValue().plusDays(1).format(formatter);
            daysItems[0] = "Today "+formattedDateTime;
            daysItems[1] = "Tmr "+formattedTmrDateTime;
            spinner.setAdapter(daysadapter);

            //view.dateView.setText(formattedDateTime);
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 1){
                    activityModel.setUIState(1);
                }else if(position == 2){
                    activityModel.setUIState(2);
                }else if(position == 3){
                    activityModel.setUIState(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection
            }
        });
        return view.getRoot();
    }
    public LocalDateTime getOffSetTime(){
        return time.getValue();
    }

    private void updateTime() {
        // Update time every second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                LocalDateTime now = LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt());

                if(now.getYear() != activityModel.getTime().getValue().getYear()) {

                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    //activityModel.removeFinished();
                }
                if(now.getMonth() != activityModel.getTime().getValue().getMonth()) {
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    //activityModel.removeFinished();
                }
                if(now.getDayOfMonth() != activityModel.getTime().getValue().getDayOfMonth()){
                    activityModel.getTime().getValue();
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    //activityModel.removeFinished();
                    //activityModel.removeFinished();
                }

                // Call this method again after 1 second
                updateTime();
            }
        }, 1000); // 1000 milliseconds = 1 second
    }




}
