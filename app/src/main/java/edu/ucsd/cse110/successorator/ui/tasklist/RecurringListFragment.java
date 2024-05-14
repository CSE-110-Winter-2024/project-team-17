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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.RecurringTasksFragmentBinding;
import edu.ucsd.cse110.successorator.databinding.TasksFragmentBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateRecurringTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;


public class RecurringListFragment extends  Fragment{

    private MainViewModel activityModel;

    private RecurringTasksFragmentBinding view;
    private RecurringListAdapter adapter;

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
        this.adapter = new RecurringListAdapter(requireContext(), List.of(),activityModel /*, id -> {
            var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        }*/);

        // Observe changes in the filtered tasks
        activityModel.getFilteredTasks().observe(cards -> {
            if(cards == null) {
                return;
            }
            List<Task> newcards = new ArrayList<Task>(cards);

            for (int i = 0; i < newcards.size(); i++) {
                //Extract the date from cards

                if (newcards.get(i).frequency() == 0) {
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
            if (cards == null) {
                return;
            }
            List<Task> newcards = new ArrayList<Task>(cards);

            for (int i = 0; i < newcards.size(); i++) {
                //Extract the date from cards

                if (newcards.get(i).frequency() == 0) {
                    newcards.set(i, null);
                }
            }
            newcards.removeIf(Objects::isNull);
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
        updateTime();





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

            formattedDateTime = activityModel.getTime().getValue().format(formatter);
            formattedTmrDateTime = activityModel.getTime().getValue().plusDays(1).format(formatter);
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

                LocalDateTime now = LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt());

                if(now.getYear() != activityModel.getTime().getValue().getYear()) {
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    activityModel.removeFinished();
                    activityModel.updateRecurrence(now);
                }
                if(now.getMonth() != activityModel.getTime().getValue().getMonth()) {
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    activityModel.removeFinished();
                    activityModel.updateRecurrence(now);
                }
                if(now.getDayOfMonth() != activityModel.getTime().getValue().getDayOfMonth()){
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    activityModel.removeFinished();
                    activityModel.updateRecurrence(now);
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
