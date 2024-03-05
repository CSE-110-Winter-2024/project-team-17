package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.TasksFragmentBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;

public class TaskListFragment extends  Fragment{

    private MainViewModel activityModel;

    private TasksFragmentBinding view;
    private TaskListAdapter adapter;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MM/dd");
    private String formattedDateTime;

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
            if (cards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = TasksFragmentBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.listView.setAdapter(adapter);
        view.dateView.setText("Test");

        view.AddButton.setOnClickListener(v -> {
            //TODO: Base on today list change the dialog
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });

        if(LocalDateTime.now() != activityModel.getTime().getValue()){
            activityModel.removeFinished();
            activityModel.timeSet(LocalDateTime.now());
        }
        updateTime();


        view.advanceButton.setOnClickListener(v -> {
            activityModel.timeAdvance();
        });

        //newly Added
        activityModel.getTime().observe(dates -> {
            if( dates == null) return;

            formattedDateTime = activityModel.getTime().getValue().format(formatter);
            view.dateView.setText(formattedDateTime);
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
