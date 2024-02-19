package edu.ucsd.cse110.successorator.ui.date;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.successorator.DateViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDateBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateFragment extends Fragment {

    private DateViewModel dateModel;
    private FragmentDateBinding view;

    private Handler handler = new Handler(Looper.getMainLooper());

    public DateFragment() {
        // Required empty public constructor
    }


    public static DateFragment newInstance() {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(DateViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.dateModel = modelProvider.get(DateViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentDateBinding.inflate(inflater, container, false);

        setupMvp();

        return view.getRoot();
    }

    private void setupMvp() {
        dateModel.getCurrentDateAndDay().observe(text -> view.dateView.setText(text));
        view.advanceButton.setOnClickListener(v -> dateModel.advanceDate());
        scheduleUpdateTask();
    }

    private void scheduleUpdateTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //observe the changing date value;
                dateModel.getCurrentDateAndDay().observe(text -> view.dateView.setText(text));
                // refreshing every 5 second
                scheduleUpdateTask();
            }
        }, 1000);
    }
}