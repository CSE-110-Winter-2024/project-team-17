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
import edu.ucsd.cse110.successorator.databinding.TmrTasksFragmentBinding;
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
        activityModel.getOrderedCards().observe(cards -> {
            var newcards = cards;
            for(int i = 0; i < newcards.size(); i++) {
                if(newcards.get(i).taskName().equals("A")){
                    newcards.remove(i);
                }
            }
            if (newcards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(newcards)); // remember the mutable copy here!
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
            formattedDateTime = activityModel.getOffSetTime().format(formatter);
            formattedTmrDateTime = activityModel.getOffSetTime().plusDays(1).format(formatter);
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

                if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()+1).getYear() != activityModel.getOffSetTime().plusDays(1).getYear()) {
                    activityModel.timeSet(LocalDateTime.now());
                    //activityModel.removeFinished();
                }
                if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()).getMonth() != activityModel.getOffSetTime().plusDays(1).getMonth()) {
                    activityModel.timeSet(LocalDateTime.now());
                    //activityModel.removeFinished();
                }
                if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()+1).getDayOfMonth() != activityModel.getOffSetTime().plusDays(1).getDayOfMonth()){
                    activityModel.timeSet(LocalDateTime.now());
                    //activityModel.removeFinished();
                    /*if(LocalDateTime.now().plusDays(activityModel.getTimeAdvCnt()+1).getDayOfMonth() != activityModel.getOffSetTime().plusDays(1).getDayOfMonth()+1 &&
                            LocalDateTime.now().getHour() > 2) {
                        activityModel.timeSet(LocalDateTime.now());
                        //activityModel.removeFinished();
                    }*/
                }

                // Call this method again after 1 second
                updateTime();
            }
        }, 1000); // 1000 milliseconds = 1 second
    }




}
