package edu.ucsd.cse110.secards.lib.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.ucsd.cse110.secards.lib.util.SimpleSubject;

public class DateInMemoryDataSource {

    private int offset_days = 0;

    private final SimpleSubject<String> date = new SimpleSubject<>();
    //Beginning of date methods/subjects getters
    public void setDate(Calendar calendar){
        SimpleDateFormat dateFormat;
        String formatted_date;
        //calendar = calendar.getInstance();
        if (calendar.get(calendar.HOUR_OF_DAY) < 2) {
            calendar.add(calendar.DAY_OF_MONTH, -1);
        }
        calendar.add(calendar.DAY_OF_MONTH, offset_days);
        dateFormat = new SimpleDateFormat("EEEE, MM/dd ss");
        formatted_date = dateFormat.format(calendar.getTime());
        date.setValue(formatted_date);
    }

    public void advanceDate(Calendar calendar){
        offset_days++;
        setDate(calendar);
    }

    public String getDate(){
        return date.getValue();
    }

    public SimpleSubject<String> getDateSubject(){
        return date;
    }
    //End of date section

    public static DateInMemoryDataSource fromDefault() {
        Calendar calendar = Calendar.getInstance();
        var data = new DateInMemoryDataSource();
        data.setDate(calendar);
        return data;
    }
}