package edu.ucsd.cse110.secards.app.ui.cardlist;

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

import edu.ucsd.cse110.secards.app.databinding.ListItemCardBinding;
import edu.ucsd.cse110.secards.lib.domain.Flashcard;

public class CardListAdapter extends ArrayAdapter<Flashcard> {

    Consumer<Integer> onChangeClick;
    Consumer<Integer> onDeleteClick;

    public CardListAdapter(Context context, List<Flashcard> flashcards, Consumer<Integer> onDeleteClick /*Consumer<Integer> onChangeClick*/) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(flashcards));
        this.onDeleteClick = onDeleteClick;
        //this.onChangeClick = onChangeClick;
    }

    public CardListAdapter(Context context, List<Flashcard> flashcards, Consumer<Integer> onDeleteClick, Consumer<Integer> onChangeClick) {
        super(context, 0, new ArrayList<>(flashcards));
        this.onDeleteClick = onDeleteClick;
        this.onChangeClick = onChangeClick; // Now included
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the flashcard for this position.
        var flashcard = getItem(position);
        assert flashcard != null;

        // Check if a view is being reused...
        ListItemCardBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemCardBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemCardBinding.inflate(layoutInflater, parent, false);
        }

        // populate the view with the flashcard's data.
        binding.cardFrontText.setText(flashcard.front());
        binding.cardFrontText.setOnClickListener(v -> {
            if (flashcard.finished()) {
                binding.cardFrontText.setPaintFlags(binding.cardFrontText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                binding.cardFrontText.setPaintFlags(binding.cardFrontText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            // strikethrough
            flashcard.flipFinished();
            onChangeClick.accept(flashcard.id());

            // prepare to reorder so the unstrikethrough would go to the end of the unstrikethrough list
            List<Flashcard> items = new ArrayList<>(getCount());
            for (int i = 0; i < getCount(); i++) {
                items.add(getItem(i));
            }

            // remove and reorder
            items.remove(flashcard);
            if (!flashcard.finished()) {
//                int firstFinishedIndex = -1;
//                for (int i = 0; i < items.size(); i++) {
//                    if (items.get(i).finished()) {
//                        firstFinishedIndex = i;
//                        break;
//                    }
//                }
//                if (firstFinishedIndex == -1) {
//                    // no unfinished task, just add
//                    items.add(flashcard);
//                } else {
//                    items.add(firstFinishedIndex, flashcard);
//                }
                items.add(0, flashcard);
            } else {

                items.add(flashcard);
            }

            // update and refresh the ListView
            clear();
            addAll(items);
            notifyDataSetChanged();

        });

        binding.cardDeleteButton.setOnClickListener(v -> {
            var id = flashcard.id();
            assert id != null;
            onDeleteClick.accept(id);
        });

        return binding.getRoot();
    }


    // The below methods aren't strictly necessary, usually.
    // But get in the habit of defining them because they never hurt
    // (as long as you have IDs for each item) and sometimes you need them.

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var flashcard = getItem(position);
        assert flashcard != null;

        var id = flashcard.id();
        assert id != null;

        return id;
    }
}
