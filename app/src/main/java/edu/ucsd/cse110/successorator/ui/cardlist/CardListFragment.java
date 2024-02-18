package edu.ucsd.cse110.successorator.ui.cardlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.ConfirmDeleteCardDialogFragment;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateCardDialogFragment;

public class CardListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private CardListAdapter adapter;

    public CardListFragment() {
        // Required empty public constructor
    }

    public static CardListFragment newInstance() {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter (with an empty list for now)
        this.adapter = new CardListAdapter(requireContext(), List.of(), id -> {
            var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        });

        activityModel.getOrderedCards().observe(cards -> {
            if (cards == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = FragmentCardListBinding.inflate(inflater, container, false);

        view.cardList.setAdapter(adapter);

        setupObservers();

        return view.getRoot();
    }

    private void setupObservers() {
        // Assuming getOrderedCards() now correctly returns a SimpleSubject<List<Flashcard>>
        activityModel.getOrderedCards().observe(cards -> {
            getActivity().runOnUiThread(() -> {
                if (cards == null || cards.isEmpty()) {
                    view.textViewNoTasks.setVisibility(View.VISIBLE);
                } else {
                    view.textViewNoTasks.setVisibility(View.GONE);
                }
                adapter.clear();
                if (cards != null) {
                    adapter.addAll(cards);
                }
                adapter.notifyDataSetChanged();
            });
        });
    }
}
