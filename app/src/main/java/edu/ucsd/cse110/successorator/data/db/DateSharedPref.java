package edu.ucsd.cse110.successorator.data.db;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.time.LocalDateTime;

public class DateSharedPref {
    private SharedPreferences sharedPreferences;
    private MutableLiveData<LocalDateTime> dateTimeLiveData = new MutableLiveData<>();

    public DateSharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // Load the initial value from SharedPreferences
        dateTimeLiveData.setValue(getLocalDateTime("myDateTime"));
    }

    public LiveData<LocalDateTime> getDateTimeLiveData() {
        return dateTimeLiveData;
    }

    public void saveLocalDateTime(String key, LocalDateTime dateTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String formattedDateTime = dateTime.toString();
        editor.putString(key, formattedDateTime);
        editor.apply();
        // Update LiveData
        dateTimeLiveData.setValue(dateTime);
    }

    public LocalDateTime getLocalDateTime(String key) {
        String formattedDateTime = sharedPreferences.getString(key, null);
        return formattedDateTime != null ? LocalDateTime.parse(formattedDateTime) : null;
    }
}

