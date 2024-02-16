//package edu.ucsd.cse110.successorator.ui.tasklist;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import java.util.ArrayList;
//import java.util.List;
//import edu.ucsd.cse110.successorator.lib.domain.Task;
//import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;
//
//
//public class TaskListFragment extends Fragment {
//    private MainViewModel activityModel;
//    private FragmentCardListBinding view;
//    private TaskListAdapter adapter;
//
//    public TaskListFragment() {
//        // Required empty public constructor
//    }
//
//    public static TaskListFragment newInstance() {
//        TaskListFragment fragment = new TaskListFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Initialize the Model
//        var modelOwner = requireActivity();
//        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
//        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
//        this.activityModel = modelProvider.get(MainViewModel.class);
//
//        // Initialize the Adapter (with an empty list for now)
//        this.adapter = new TaskListAdapter(requireContext(), List.of());
//        activityModel.getOrderedCards().observe(cards -> {
//            if (cards == null) return;
//            adapter.clear();
//            adapter.addAll(new ArrayList<>()); // remember the mutable copy here!
//            adapter.notifyDataSetChanged();
//        });
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        this.view = FragmentTaskListBinding.inflate(inflater, container, false);
//
//        // Set the adapter on the ListView
//        view.TaskList.setAdapter(adapter);
//
//        //Show CreateCardDialogFragment
//        view.createTaskButton.setOnClickListener(v -> {
//            var dialogFragment = CreateTaskDialogFragment.newInstance();
//            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFragment");
//
//        });
//
//        return view.getRoot();
//    }
//}