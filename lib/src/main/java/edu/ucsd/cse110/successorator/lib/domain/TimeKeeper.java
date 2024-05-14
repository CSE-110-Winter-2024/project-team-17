package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TimeKeeper {
    Subject<LocalDateTime> getDateTime();
    void setDateTime(LocalDateTime dateTime);
}
