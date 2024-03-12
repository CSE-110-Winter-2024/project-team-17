package edu.ucsd.cse110.successorator;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.ContextMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.PendingListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.RecurringListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.TomorrowListFragment;

public class MainActivity extends AppCompatActivity {

    private int listNum = 0;

    private MainViewModel viewModel;

    private ActivityMainBinding view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(/*R.string.app_title*/"Successorator");

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        ImageButton focusModeButton = findViewById(R.id.focus_mode_button); // Make sure you have this button in activity_main.xml
        focusModeButton.setOnClickListener(v -> showFilterDialog());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getUIState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer newState) {
                switch (newState) {
                    case 0:
                        // Replace fragment or switch to UI 2
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, TaskListFragment.newInstance())
                                .commit();
                        break;
                    case 1:
                        // Replace fragment or switch to UI 1
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, TomorrowListFragment.newInstance())
                                .commit();
                        break;
                    case 2:
                        // Replace fragment or switch to UI 2
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, PendingListFragment.newInstance())
                                .commit();
                        break;
                    case 3:
                        // Replace fragment or switch to UI 2
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, RecurringListFragment.newInstance())
                                .commit();
                        break;
                    // Add more cases as needed for additional UI states
                }
            }
        });
    }

    private void showFilterDialog() {
        final String[] items = {"Home", "Work", "School", "Errand", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Context")
                .setItems(items, (dialog, which) -> {
                    ImageButton focusModeButton = findViewById(R.id.focus_mode_button);
                    if (which >= 0 && which < items.length - 1) { // Contexts Home, Work, School, Errand
                        viewModel.setContextFilter(items[which].charAt(0));
                        // Set the gray background
                        focusModeButton.setBackgroundResource(R.drawable.button_gray_background);
                    } else { // 'Cancel' or any other condition
                        viewModel.setContextFilter(null); // Clear filter
                        // Reset the background (replace with any default or remove background)
                        focusModeButton.setBackgroundResource(0); // 0 will remove the background
                    }
                });
        builder.create().show();
    }

}
