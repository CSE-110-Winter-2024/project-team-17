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
import edu.ucsd.cse110.successorator.databinding.TmrTasksFragmentBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTomorrowTaskDialogFragment;

public class TomorrowListFragment extends  Fragment{

    private MainViewModel activityModel;

    private TmrTasksFragmentBinding view;
    private TomorrowListAdapter adapter;

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


    public TomorrowListFragment() {

    }

    public static TomorrowListFragment newInstance() {
        TomorrowListFragment fragment = new TomorrowListFragment();
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
        this.adapter = new TomorrowListAdapter(requireContext(), List.of(),activityModel /*, id -> {
            var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        }*/);

        activityModel.getFilteredTasks().observe(cards -> {
            //Determine the current date as a string
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime tomorrowDateTime = currentDateTime.plusDays(1);
            int year = tomorrowDateTime.getYear();
            int month = tomorrowDateTime.getMonthValue(); // Month value is 1-based
            int dayOfMonth = tomorrowDateTime.getDayOfMonth();
            DayOfWeek dayOfWeek = tomorrowDateTime.getDayOfWeek();
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
            String tmrDate = Integer.toString(dayOfWeekValue) + monthStr + dayStr + Integer.toString(year);
            List<Task> newcards = new ArrayList<Task>(cards);

            //THERE IS ERROR HERE TRYING TO FIX!!!
            //UNABLE TO CORRECTLY FILTER OUT TOMORROWS TASKS
            //Trying to fix now
            for (int i = 0; i < newcards.size(); i++) {
                //Extract the date from cards
                String currDate = newcards.get(i).currOccurDate();
//                if (newcards.get(i).frequency() != 0 && newcards.get(i).finished())
//                    newcards.get(i).flipFinished();
                if (tmrDate.compareTo(currDate) != 0 && newcards.get(i).frequency() != 1) {
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


        activityModel.getOrderedCards().observe(cards -> {

            //Determine the current date as a string
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime tomorrowDateTime = currentDateTime.plusDays(1);
            int year = tomorrowDateTime.getYear();
            int month = tomorrowDateTime.getMonthValue(); // Month value is 1-based
            int dayOfMonth = tomorrowDateTime.getDayOfMonth();
            DayOfWeek dayOfWeek = tomorrowDateTime.getDayOfWeek();
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
            String tmrDate = Integer.toString(dayOfWeekValue) + monthStr + dayStr + Integer.toString(year);
            if (cards == null) {
                return;
            }
            List<Task> newcards = new ArrayList<Task>(cards);

            for (int i = 0; i < newcards.size(); i++) {

                //Extract the date from cards
                String currDate = newcards.get(i).currOccurDate();
//                if (newcards.get(i).frequency() != 0 && newcards.get(i).finished())
//                    newcards.get(i).flipFinished();
                if (tmrDate.compareTo(currDate) != 0 && newcards.get(i).frequency() != 1) {
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
        this.view = TmrTasksFragmentBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.listView.setAdapter(adapter);
        //view.dateView.setText("Test");
        view.AddButton.setOnClickListener(v -> {
            var dialogFragment = CreateTomorrowTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTomorrowTaskDialogFragment");
        });
        String[] item = {activityModel.getTime().getValue().format(formatter),
                //TODO: When creating item must use the getOffsetTime due to Advance time
                activityModel.getTime().getValue().plusDays(1).format(formatter), "Pending","Recurring"};

        /*if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()) != activityModel.getTime().getValue()){
            //activityModel.removeFinished();
            activityModel.timeSet(LocalDateTime.now());
            String[] newItem = {activityModel.getTime().getValue().format(formatter),
                    activityModel.getTime().getValue().plusDays(1).format(formatter), "Pending","Recurring"};
            item = newItem;
        }*/
        updateTime();




        view.advanceButton.setOnClickListener(v -> {
            //activityModel.setUIState(2);
            activityModel.timeAdvance();
        });

        //newly Added
        String[] daysItems = {"Today "+formattedDateTime, "Tmr "+formattedTmrDateTime, "Pending", "Recurring"};
        spinner = view.spinner;
        ArrayAdapter<String> daysadapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, daysItems);
        daysadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(daysadapter);
        spinner.setSelection(1);
        activityModel.getTime().observe(dates -> {
            if( dates == null) return;
            //TODO: Change to get OffsetTime so tthe time change when change list
            formattedDateTime = activityModel.getTime().getValue().format(formatter);
            formattedTmrDateTime = activityModel.getTime().getValue().plusDays(1).format(formatter);
            daysItems[0] = "Today "+formattedDateTime;
            daysItems[1] = "Tmr "+formattedTmrDateTime;
            spinner.setAdapter(daysadapter);
            spinner.setSelection(1);


            //view.dateView.setText(formattedDateTime);
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    activityModel.setUIState(0);
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

    private void updateTime() {
        // Update time every second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO: need to fix the bug of offset time, as the removing task is still unknown for now

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
                    activityModel.getTime().getValue();
                    activityModel.timeSet(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()));
                    activityModel.removeFinished();
                    activityModel.updateRecurrence(now);
                    //TODO: Commented out the 2am restraint for simplicity
                    if(now.getDayOfMonth() != activityModel.getTime().getValue().getDayOfMonth()+1 &&
                            now.getHour() > 2) {
                        activityModel.timeSet(LocalDateTime.now());
                        activityModel.removeFinished();
                        activityModel.updateRecurrence(now);
                    }
                }

                // Call this method again after 1 second
                updateTime();
            }
        }, 1000); // 1000 milliseconds = 1 second
    }




}
