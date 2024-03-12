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
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.PendingTasksFragmentBinding;
import edu.ucsd.cse110.successorator.databinding.TasksFragmentBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreatePendingTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;


public class PendingListFragment extends  Fragment{

    private MainViewModel activityModel;

    private PendingTasksFragmentBinding view;
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


    public PendingListFragment() {

    }

    public static PendingListFragment newInstance() {
        PendingListFragment fragment = new PendingListFragment();
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

        // Observe changes in the filtered tasks
        activityModel.getFilteredTasks().observe(cards -> {
            List<Task> newcards = new ArrayList<Task>(cards);
            for (int i = 0; i < newcards.size(); i++) {
                //Extract the date from cards
                String currDate = newcards.get(i).currOccurDate();
                if (newcards.get(i).finished()) {
                    newcards.remove(i);
                }
            }
            if (newcards == null) return;

            Character currentFilter = activityModel.getContextFilter().getValue();
            // Apply both the context filter and the specific date logic for this fragment
            List<Task> filteredTasks = newcards.stream()
                    .filter(task -> currentFilter == null || task.tag() == currentFilter) // Context filtering logic
                    // Add here any additional filtering specific to this fragment, e.g., date-based filtering
                    .collect(Collectors.toList());

            adapter.clear();
            adapter.addAll(new ArrayList<>(filteredTasks)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });

        activityModel.getOrderedCards().observe(cards -> {
            List<Task> newcards = new ArrayList<Task>(cards);
            for (int i = 0; i < newcards.size(); i++) {
                //Extract the date from cards
                String currDate = newcards.get(i).currOccurDate();
                if (newcards.get(i).finished()) {
                    newcards.remove(i);
                }
            }
            if (newcards == null) return;

            Character currentFilter = activityModel.getContextFilter().getValue();
            // Apply both the context filter and the specific date logic for this fragment
            List<Task> filteredTasks = newcards.stream()
                    .filter(task -> currentFilter == null || task.tag() == currentFilter) // Context filtering logic
                    // Add here any additional filtering specific to this fragment, e.g., date-based filtering
                    .collect(Collectors.toList());

            adapter.clear();
            adapter.addAll(new ArrayList<>(filteredTasks)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = PendingTasksFragmentBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.listView.setAdapter(adapter);
        //view.dateView.setText("Pending");
        view.AddButton.setOnClickListener(v -> {
            //TODO: Base on today list change the dialog
            var dialogFragment = CreatePendingTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreatePendingTaskDialogFragment");
        });
        String[] item = {activityModel.getTime().getValue().format(formatter),
                activityModel.getTime().getValue().plusDays(1).format(formatter), "Pending","Recurring"};
        updateTime();




        view.advanceButton.setOnClickListener(v -> {
            activityModel.timeAdvance();
            //activityModel.setUIState(3);
        });

        //newly Added

        String[] daysItems = {"Today "+formattedDateTime, "Tmr "+formattedTmrDateTime, "Pending", "Recurring"};
        spinner = view.spinner;

        ArrayAdapter<String> daysadapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, daysItems);
        daysadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(daysadapter);
        spinner.setSelection(2);
        activityModel.getTime().observe(dates -> {
            if( dates == null) return;

            formattedDateTime = activityModel.getTime().getValue().format(formatter);
            formattedTmrDateTime = activityModel.getTime().getValue().plusDays(1).format(formatter);
            daysItems[0] = "Today "+formattedDateTime;
            daysItems[1] = "Tmr "+formattedTmrDateTime;
            spinner.setAdapter(daysadapter);
            spinner.setSelection(2);
            //view.dateView.setText(formattedDateTime);
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    activityModel.setUIState(0);
                }else if(position == 1){
                    activityModel.setUIState(1);
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

    private void updateTime() {
        // Update time every second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                LocalDateTime now = LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt());

                if(now.getYear() != activityModel.getTime().getValue().getYear()) {
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    activityModel.removeFinished();
                }
                if(now.getMonth() != activityModel.getTime().getValue().getMonth()) {
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    activityModel.removeFinished();
                }
                if(now.getDayOfMonth() != activityModel.getTime().getValue().getDayOfMonth()){
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    activityModel.removeFinished();
                    //TODO: Commented out the 2am restraint for simplicity
                    /*if(now.getDayOfMonth() != activityModel.getTime().getValue().getDayOfMonth()+1 &&
                            now.getHour() > 2) {
                        activityModel.timeSet(LocalDateTime.now());
                        activityModel.removeFinished();
                    }*/
                }

                // Call this method again after 1 second
                updateTime();
            }
        }, 1000); // 1000 milliseconds = 1 second
    }




}
