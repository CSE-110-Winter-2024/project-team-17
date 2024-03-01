package edu.ucsd.cse110.successorator.lib.domain;


import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.util.Subject;
public class SimpleTimeRepository implements TimeKeeper{
    @Override
    public Subject<LocalDateTime> getDateTime() {

        return null;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {

    }
}
