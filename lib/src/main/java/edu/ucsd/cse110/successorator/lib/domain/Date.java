package edu.ucsd.cse110.successorator.lib.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Date {

    private Integer date;
    private Integer month;

    LocalDateTime currentDateTime;
    DayOfWeek dayOfWeek;

    public Date(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
        dayOfWeek = currentDateTime.getDayOfWeek();
        month = currentDateTime.getMonthValue();
        date = currentDateTime.getDayOfMonth();
    }



    public void setDate(int date) {
        this.date = date;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public String getDate(){
        return date.toString();
    }

    public String getWeekday(){
        return dayOfWeek.toString();
    }

    public String getMonth(){
        return month.toString();
    }

}
