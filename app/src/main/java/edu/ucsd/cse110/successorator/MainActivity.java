package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.Tasks;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private ArrayList<Task> taskList;
    private ArrayAdapter<Task> taskArrayAdapter;
    private ListView listView;
    private Button new_task_button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Successorator");

        //Temporary TaskList just to make sure the app runs
        //REPLACE WITH USER STORY 2
        taskList = new ArrayList<>();
        // Initialize the Model
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        new_task_button = findViewById(R.id.new_task_button);

        //The newTask Button will add a Task object to the backend TaskList

        new_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TEMPORARY JUST TO MAKE SURE THE APP RUNS
                showCreateTaskDialog();
            }
        });
    }
    private void showCreateTaskDialog() {
        CreateTaskDialogFragment dialogFragment = CreateTaskDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "CreateTaskDialogFragment");
    }


    //Testing for



















    /*private ActivityMainBinding view;
    private Handler handler = new Handler(Looper.getMainLooper());
    private DateViewModel model;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        var dateSource = DateInMemoryDataSource.fromDefault();
        this.model = new DateViewModel(new DateRepository(dateSource));

        this.view = ActivityMainBinding.inflate(getLayoutInflater());

        model.getCurrentDateAndDay().observe(text -> view.dateView.setText(text));

        //var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        //view.placeholderText.setText(getDate());
        //view.dateView.setText(getDate());

        setContentView(view.getRoot());
        scheduleUpdateTask();
    }*/

    /*private void scheduleUpdateTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Assuming some logic to update the date value
                model.getCurrentDateAndDay().observe(text -> view.dateView.setText(text));
                // Repeat every 5 seconds (for demonstration purposes)
                scheduleUpdateTask();
            }
        }, 1000);
    }*/


    /*protected String getDate(){
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();


        if (calendar.get(Calendar.HOUR_OF_DAY) <= 2) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        dateFormat = new SimpleDateFormat("EEEE, MM/dd");
        date = dateFormat.format(calendar.getTime());
        return date;
    }*/
}

