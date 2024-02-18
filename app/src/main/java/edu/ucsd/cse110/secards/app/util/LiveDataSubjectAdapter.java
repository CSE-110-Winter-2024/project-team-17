package edu.ucsd.cse110.secards.app.util;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import edu.ucsd.cse110.secards.lib.util.Observer;
import edu.ucsd.cse110.secards.lib.util.Subject;

public class LiveDataSubjectAdapter<T> implements Subject<T> {
    private final LiveData<T> adaptee;

    public LiveDataSubjectAdapter(LiveData<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Nullable
    @Override
    public T getValue() {
        return adaptee.getValue();
    }

    @Override
    public void observe(Observer<T> observer) {
        adaptee.observeForever(observer::onChanged);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        adaptee.removeObserver(observer::onChanged);
    }
}