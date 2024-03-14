package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime; // Import the LocalDateTime class from java.time package
import java.time.DayOfWeek; // Import the DayOfWeek enum from java.time package


import java.io.Serializable;
import java.util.Objects;


public class Task {

    private final @Nullable Integer id;
    private int sortOrder;
    private final String taskName;
    private boolean finished = false;
    private String addedDate;
    private String currOccurDate;
    private String nextOccurDate;
    private int frequency;
    private char tag;


    public Task (Integer id, String taskName, int sortOrder) {
        this.id = id;
        this.taskName = taskName;
        this.sortOrder = sortOrder;
    }

    public Task (Integer id, String taskName, int sortOrder, boolean finished, String addedDate, int frequency, char tag) {
        this.id = id;
        this.taskName = taskName;
        this.sortOrder = sortOrder;
        this.finished = finished;
        this.addedDate = addedDate;
        this.frequency = frequency;
        this.tag = tag;
        this.currOccurDate = new String(addedDate);
        this.nextOccurDate = null;
        calculateRecurrence(); //nextOccurDate should be set
    }

    public @Nullable Integer id() {
        return id;
    }

    public @NonNull String taskName() {
        return taskName;
    }


    public int sortOrder() {
        return sortOrder;
    }

    public boolean finished() {
        return finished;
    }

    public void flipFinished() {
        this.finished = !finished;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Task withId(int id) {
        return new Task(id, this.taskName, this.sortOrder, this.finished, this.addedDate, this.frequency, this.tag);
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(this.id, this.taskName, sortOrder, this.finished, this.addedDate, this.frequency, this.tag);
    }

    public Task withFinished(boolean finished) {
        return new Task(this.id, this.taskName, this.sortOrder, finished, this.addedDate, this.frequency, this.tag);
    }

    public void updateRecurrence() {
        //Todays date in the string format
        //Can call this to update currOccurDate and nextOccurDate?
        //We will set if the task is unfinished, curroccurdate just rolls over to the next day, while we will calculate the next supposed occurdate and e
        //Set that to nextOccurdate
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime previousDateTime = currentDateTime.minusDays(1);

        LocalDate currentDate = LocalDate.now();
        String originalDate = this.addedDate;
        String prevDateString = dateToString(previousDateTime);
        int originalDayOfWeek = (int)(originalDate.charAt(0));

        //This is for the case when we are not supposed to calculate the new occurence date, but we need to
        //Account for the rollover to new date if the task is unfinished
        if (!prevDateString.equals(this.nextOccurDate)) { //If its the same, then we know its time to update
            if (!this.finished) {
                this.currOccurDate = dateToString(currentDateTime);
            }
            return;
        }
        // Get the first day of the month
        LocalDate nextMonthDate = currentDate.plusMonths(1);
        LocalDate firstDayOfNextMonth = nextMonthDate.withDayOfMonth(1);
        // Get the day of the week for the first day of the month
        DayOfWeek firstDayOfWeek = firstDayOfNextMonth.getDayOfWeek();
        int dayOfWeekNumber = firstDayOfWeek.getValue(); //The DayofWeek of the first date of the month



        if (this.frequency == 0) {
            //No recurrence
            //Delete the task if finished otherwise turn over to next day
            this.currOccurDate = dateToString(currentDateTime);
            return;
        }
        else if (this.frequency < 0) { //Monthly recurrence

            int weekNum = this.frequency * -1; //Which week in the month
            int newMonth = 0;
            int newDate = 0;
            int newYear = 0;

            if (weekNum == 5) {//Special case for week 5 recurrences
                /*In the case of week 5, there are no months where there are two 5th weeks in a row
                //Proposal: First try and set it to the 5th week of this month
                If that is not possible then roll over to next months 1st week*/
                if (this.addedDate.compareTo(this.currOccurDate) == 0) {//When its the first time the recurrence is updated MAYBE WE CAN PUT THIS IN TASKS
                    //First time the recurrence is updated
                    //Set currOccurDate to the following following months first week
                    LocalDate nextNextMonthDate = currentDate.plusMonths(2);
                    weekNum = 1;
                    LocalDate firstDayOfNextNextMonth = nextNextMonthDate.withDayOfMonth(1);
                    // Get the day of the week for the first day of the month
                    firstDayOfWeek = firstDayOfNextNextMonth.getDayOfWeek();
                    int firstDayNum = firstDayOfWeek.getValue(); //The DayofWeek of the first date of the month

                    if (originalDayOfWeek < firstDayNum) {
                        weekNum++; //In the case if the day we want to occur on is before the first day of the month
                    }
                    newDate = (originalDayOfWeek - firstDayNum + 1) + (weekNum-1)*7;
                    newMonth = nextNextMonthDate.getMonthValue();
                    newYear = nextNextMonthDate.getYear();
                }
                else {
                    int currDate = currentDate.getDayOfMonth();
                    LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
                    int firstDayNum = firstDayOfMonth.getDayOfWeek().getValue();

                    //Try to assign it to this months 5th week
                    newDate = (originalDayOfWeek - firstDayNum + 1) + (weekNum-1)*7;
                    if (newDate > firstDayOfMonth.lengthOfMonth() || newDate == currDate) {
                        //Set currOccurDate to the following following months first week
                        weekNum = 1;
                        LocalDate firstDayOfNextNextMonth = nextMonthDate.withDayOfMonth(1);
                        // Get the day of the week for the first day of the month
                        firstDayOfWeek = firstDayOfNextNextMonth.getDayOfWeek();
                        firstDayNum = firstDayOfWeek.getValue(); //The DayofWeek of the first date of the month

                        if (originalDayOfWeek < firstDayNum) {
                            weekNum++; //In the case if the day we want to occur on is before the first day of the month
                        }
                        newDate = (originalDayOfWeek - firstDayNum + 1) + (weekNum-1)*7;
                        newMonth = nextMonthDate.getMonthValue();
                        newYear = nextMonthDate.getYear();

                    }
                }
            }
            else {
                int firstDayNum = dayOfWeekNumber;
                if (originalDayOfWeek < firstDayNum) {
                    weekNum++; //In the case if the day we want to occur on is before the first day of the month
                }
                newDate = (originalDayOfWeek - firstDayNum + 1) + (weekNum-1)*7;
                newMonth = nextMonthDate.getMonthValue();
                newYear = nextMonthDate.getYear();
            }

            String newOccurDate = Integer.toString(originalDayOfWeek)
                        + Integer.toString(newMonth) + Integer.toString(newDate) + Integer.toString(newYear);
            this.nextOccurDate = newOccurDate;

        }
        else if (this.frequency == 1) {
            //Daily recurrence
            //Show everyday
            String newOccurDate = dateToString(currentDateTime);
            this.nextOccurDate = newOccurDate;

        }
        else if (this.frequency == 7) {
            //Weekly
            //LocalDate oneWeekLater = currentDate.plusWeeks(1);
            LocalDateTime oneWeekLater = previousDateTime.plusWeeks(1);
            int newWeekDay = oneWeekLater.getDayOfWeek().getValue();
            int newMonth = oneWeekLater.getMonthValue();
            int newDate = oneWeekLater.getDayOfMonth();
            int newYear = oneWeekLater.getYear();
            String newOccurDate = Integer.toString(newWeekDay)
                    + Integer.toString(newMonth) + Integer.toString(newDate) + Integer.toString(newYear);
            this.nextOccurDate = newOccurDate;
        }

        //If the task is unfinished then the curroccurdate should just roll over, otherwise it is set to nextoccurdate
        if (this.finished) {
            this.currOccurDate = this.nextOccurDate;
            this.flipFinished();
        }
        else {
            this.currOccurDate = dateToString(currentDateTime);
        }
    }

    //This is only used to initialize the nextoccurdate when the task is first created
    public void calculateRecurrence() {
        //Todays date in the string format
        //Can call this to update currOccurDate and nextOccurDate?
        //We will set if the task is unfinished, curroccurdate just rolls over to the next day, while we will calculate the next supposed occurdate and e
        //Set that to nextOccurdate
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime previousDateTime = currentDateTime.minusDays(1);

        LocalDate currentDate = LocalDate.now();
        String originalDate = this.addedDate;
        String prevDateString = dateToString(previousDateTime);
        int originalDayOfWeek = (int)(originalDate.charAt(0));

        // Get the first day of the month
        LocalDate nextMonthDate = currentDate.plusMonths(1);
        LocalDate firstDayOfNextMonth = nextMonthDate.withDayOfMonth(1);
        // Get the day of the week for the first day of the month
        DayOfWeek firstDayOfWeek = firstDayOfNextMonth.getDayOfWeek();
        int dayOfWeekNumber = firstDayOfWeek.getValue(); //The DayofWeek of the first date of the month



        if (this.frequency == 0) {
            //No recurrence
            return;
        }
        else if (this.frequency < 0) { //Monthly recurrence

            int weekNum = this.frequency * -1; //Which week in the month
            int newMonth = 0;
            int newDate = 0;
            int newYear = 0;

            if (weekNum == 5) {//Special case for week 5 recurrences
                /*In the case of week 5, there are no months where there are two 5th weeks in a row
                //Proposal: First try and set it to the 5th week of this month
                If that is not possible then roll over to next months 1st week*/
                if (this.addedDate.compareTo(this.currOccurDate) == 0) {//When its the first time the recurrence is updated MAYBE WE CAN PUT THIS IN TASKS
                    //First time the recurrence is updated
                    //Set currOccurDate to the following following months first week
                    LocalDate nextNextMonthDate = currentDate.plusMonths(2);
                    weekNum = 1;
                    LocalDate firstDayOfNextNextMonth = nextNextMonthDate.withDayOfMonth(1);
                    // Get the day of the week for the first day of the month
                    firstDayOfWeek = firstDayOfNextNextMonth.getDayOfWeek();
                    int firstDayNum = firstDayOfWeek.getValue(); //The DayofWeek of the first date of the month

                    if (originalDayOfWeek < firstDayNum) {
                        weekNum++; //In the case if the day we want to occur on is before the first day of the month
                    }
                    newDate = (originalDayOfWeek - firstDayNum + 1) + (weekNum-1)*7;
                    newMonth = nextNextMonthDate.getMonthValue();
                    newYear = nextNextMonthDate.getYear();
                }

            } else {
                int firstDayNum = dayOfWeekNumber;
                if (originalDayOfWeek < firstDayNum) {
                    weekNum++; //In the case if the day we want to occur on is before the first day of the month
                }
                newDate = (originalDayOfWeek - firstDayNum + 1) + (weekNum-1)*7;
                newMonth = nextMonthDate.getMonthValue();
                newYear = nextMonthDate.getYear();
            }

            String newOccurDate = Integer.toString(originalDayOfWeek)
                    + Integer.toString(newMonth) + Integer.toString(newDate) + Integer.toString(newYear);
            this.nextOccurDate = newOccurDate;

        }
        else if (this.frequency == 1) {
            //Daily recurrence
            //Show everyday
            LocalDateTime tomorrowDateTime = currentDateTime.plusDays(1);
            String newOccurDate = dateToString(tomorrowDateTime);
            this.nextOccurDate = newOccurDate;

        }
        else if (this.frequency == 7) {
            //Weekly
            LocalDate oneWeekLater = currentDate.plusWeeks(1);
            int newWeekDay = oneWeekLater.getDayOfWeek().getValue();
            int newMonth = oneWeekLater.getMonthValue();
            int newDate = oneWeekLater.getDayOfMonth();
            int newYear = oneWeekLater.getYear();
            String newOccurDate = Integer.toString(newWeekDay)
                    + Integer.toString(newMonth) + Integer.toString(newDate) + Integer.toString(newYear);
            this.nextOccurDate = newOccurDate;
        }

    }

    public void setCurrOccurDate(String dateString) {
        this.currOccurDate = dateString;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(taskName, task.taskName) &&
                sortOrder == task.sortOrder &&
                finished == task.finished;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, sortOrder);
    }

    public void setSortOrder(int i) {
        this.sortOrder = i;
    }

    public String addedDate() {
        return this.addedDate;
    }

    public String currOccurDate() { return this.currOccurDate;}

    public String nextOccurDate() { return this.nextOccurDate;}

    public int frequency() {
        return this.frequency;
    }

    public char tag() {
        return this.tag;
    }

    public String dateToString(LocalDateTime currentDateTime) {

        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonthValue(); // Month value is 1-based
        int dayOfMonth = currentDateTime.getDayOfMonth();
        DayOfWeek dayOfWeek = currentDateTime.getDayOfWeek();
        // You can then use the DayOfWeek enum directly, or you can get its value as an int if needed
        int dayOfWeekValue = dayOfWeek.getValue(); // Monday is 1, Sunday is 7

        int[] dateArray = new int[4];
        dateArray[0] = dayOfWeekValue;
        dateArray[1] = month;
        dateArray[2] = dayOfMonth;
        dateArray[3] = year;
        String monthStr;
        if (month < 10) {
            monthStr = "0" + Integer.toString(month);
        }
        else {
            monthStr = Integer.toString(month);
        }
        String dayStr;
        if (dayOfMonth < 10) {
            dayStr = "0" + Integer.toString(dayOfMonth);
        }
        else {
            dayStr = Integer.toString(dayOfMonth);
        }
        return Integer.toString(dayOfWeekValue) + monthStr + dayStr + Integer.toString(year);

    }
    public void setDate(String newDate){ this.currOccurDate = newDate;}


}
