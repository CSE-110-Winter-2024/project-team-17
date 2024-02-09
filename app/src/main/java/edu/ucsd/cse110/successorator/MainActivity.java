package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Successorator");

        // Initialize the Model
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

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

