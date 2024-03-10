package edu.ucsd.cse110.successorator.ui.tasklist;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.TaskItemBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
public class RecurringListAdapter extends ArrayAdapter<Task> {

    Consumer<Integer> onChangeClick;
    MainViewModel activityModel;

    public RecurringListAdapter(
            Context context,
            List<Task> flashcards,
            Consumer<Integer> onChangeClick
    ) {
        super(context, 0, new ArrayList<>(flashcards));
        this.onChangeClick = onChangeClick;
    }

    public RecurringListAdapter(
            Context context,
            List<Task> flashcards,
            MainViewModel activityModel
    ) {
        super(context, 0, new ArrayList<>(flashcards));
        this.onChangeClick = onChangeClick;
        this.activityModel = activityModel;
    }

    public RecurringListAdapter(Context context, List<Task> tasks) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(tasks));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the flashcard for this position.
        var task = getItem(position);
        assert task != null;

        // Check if a view is being reused...
        TaskItemBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = TaskItemBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = TaskItemBinding.inflate(layoutInflater, parent, false);
        }


        // Populate the view with the flashcard's data.
        binding.textView2.setText(task.taskName());
        if(!task.finished()){
            binding.textView2.setPaintFlags(0);
        }else{
            binding.textView2.setPaintFlags(
                    binding.textView2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        binding.textView2.setOnLongClickListener(v -> {
            if(!task.finished()){
                binding.textView2.setPaintFlags(
                        binding.textView2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                binding.textView2.setPaintFlags(0);
            }
            task.flipFinished();
            //TODO: bug
            activityModel.reorder(task);

            //not sure to use or not yet.
            /*var id = task.id();
            assert id != null;
            onChangeClick.accept(id);*/
            return true;
        });

        return binding.getRoot();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var task = getItem(position);
        assert task != null;

        var id = task.id();
        assert id != null;

        return id;
    }



}
