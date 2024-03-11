package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.RecurringTasksFragmentBinding;
import edu.ucsd.cse110.successorator.databinding.TasksFragmentBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateRecurringTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;


public class RecurringListFragment extends  Fragment{

    private MainViewModel activityModel;

    private RecurringTasksFragmentBinding view;
    private TaskListAdapter adapter;

    private ArrayList<String> dateMenu;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItems;

    private Spinner spinner;

    //private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MM/dd");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd");
    private String formattedDateTime;

    private String formattedTmrDateTime;

    private Handler handler = new Handler();

    private String mode = "today";


    public RecurringListFragment() {

    }

    public static RecurringListFragment newInstance() {
        RecurringListFragment fragment = new RecurringListFragment();
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
            if (cards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = RecurringTasksFragmentBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.listView.setAdapter(adapter);
        //view.dateView.setText("Recurring");
        view.AddButton.setOnClickListener(v -> {
            //TODO: Base on today list change the dialog
            var dialogFragment = CreateRecurringTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateRecurringTaskDialogFragment");
        });
        String[] item = {activityModel.getTime().getValue().format(formatter),
                activityModel.getTime().getValue().plusDays(1).format(formatter), "Pending","Recurring"};





        view.advanceButton.setOnClickListener(v -> {
            //activityModel.setUIState(0);
            activityModel.timeAdvance();
        });

        //newly Added

        String[] daysItems = {"Today "+formattedDateTime, "Tmr "+formattedTmrDateTime, "Pending", "Recurring"};
        spinner = view.spinner;

        ArrayAdapter<String> daysadapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, daysItems);
        daysadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(daysadapter);
        spinner.setSelection(3);

        activityModel.getTime().observe(dates -> {
            if( dates == null) return;

            formattedDateTime = activityModel.getOffSetTime().format(formatter);
            formattedTmrDateTime = activityModel.getOffSetTime().plusDays(1).format(formatter);
            daysItems[0] = "Today "+formattedDateTime;
            daysItems[1] = "Tmr "+formattedTmrDateTime;
            spinner.setAdapter(daysadapter);
            spinner.setSelection(3);
            //view.dateView.setText(formattedDateTime);

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    activityModel.setUIState(0);
                }else if(position == 2){
                    activityModel.setUIState(2);
                }else if(position == 1){
                    activityModel.setUIState(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection
            }
        });








        return view.getRoot();
    }

    private void updateTime() {
        // Update time every second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()).getYear() != activityModel.getTime().getValue().getYear()) {
                    activityModel.timeSet(LocalDateTime.now());
                    activityModel.removeFinished();
                }
                if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()).getMonth() != activityModel.getTime().getValue().getMonth()) {
                    activityModel.timeSet(LocalDateTime.now());
                    activityModel.removeFinished();
                }
                if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()).getDayOfMonth() != activityModel.getTime().getValue().getDayOfMonth()){
                    if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()).getDayOfMonth() != activityModel.getTime().getValue().getDayOfMonth()+1 &&
                            LocalDateTime.now().getHour() > 2) {
                        activityModel.timeSet(LocalDateTime.now());
                        activityModel.removeFinished();
                    }
                }

                // Call this method again after 1 second
                updateTime();
            }
        }, 1000); // 1000 milliseconds = 1 second
    }




}