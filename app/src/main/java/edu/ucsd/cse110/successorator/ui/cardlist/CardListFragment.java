package edu.ucsd.cse110.successorator.ui.cardlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import edu.ucsd.cse110.successorator.lib.data.DateInMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.DateRepository;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.ConfirmDeleteCardDialogFragment;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateCardDialogFragment;

public class CardListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding view;
    private CardListAdapter adapter;

    private DateInMemoryDataSource dateSource = DateInMemoryDataSource.fromDefault();
    private DateRepository dateRepo = new DateRepository(dateSource);

    private Handler handler = new Handler(Looper.getMainLooper());


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
        this.adapter = new CardListAdapter(requireContext(), List.of(),
                id -> {
                    var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
                    dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
                },
                // This is the lambda function for handling changes in card's finished state
                id -> activityModel.refreshOrderedCards() // Assuming refreshOrderedCards() is a method in MainViewModel
        );

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

        // Set the adapter on the ListView
        view.cardList.setAdapter(adapter);

        view.createCardButton.setOnClickListener(v -> {
            var dialogFragment = CreateCardDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFormat");
        });
        view.dateView.setText(dateRepo.getDate());
        view.advanceButton.setOnClickListener(v ->{
            dateRepo.advanceDate();
            view.dateView.setText(dateRepo.getDate());
            var cards = activityModel.getOrderedCards().getValue();
            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i).finished()){
                    activityModel.remove(cards.get(i).id());
                }
            }
            adapter.clear();
            adapter.addAll(new ArrayList<>(cards)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();

        });
        scheduleUpdateTask();
        setupObservers();

        return view.getRoot();
    }

    private void scheduleUpdateTask() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //observe the changing date value;
                view.dateView.setText(dateRepo.getDate());
                // refreshing every 5 second
                scheduleUpdateTask();
            }
        }, 1000);
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
