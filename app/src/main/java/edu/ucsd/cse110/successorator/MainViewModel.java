package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Flashcard;
import edu.ucsd.cse110.successorator.lib.domain.FlashcardRepository;
import edu.ucsd.cse110.successorator.lib.domain.Flashcards;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final FlashcardRepository flashcardRepository;
    private final MutableSubject<List<Flashcard>> orderedCards;
    private final MutableSubject<Flashcard> topCard;
    private final MutableSubject<Boolean> isShowingFront;
    private final MutableSubject<String> displayedText;

    public static final ViewModelInitializer<MainViewModel> initializer =
        new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getFlashcardRepository());
            });

    public MainViewModel(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;

        // Create the observable subjects.
        this.orderedCards = new SimpleSubject<>();
        this.topCard = new SimpleSubject<>();
        this.isShowingFront = new SimpleSubject<>();
        this.displayedText = new SimpleSubject<>();

        // Initialize...
        isShowingFront.setValue(true);

        // When the list of cards changes (or is first loaded), reset the ordering.
        flashcardRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Flashcard::sortOrder))
                    .collect(Collectors.toList());

            orderedCards.setValue(newOrderedCards);
        });

        // When the ordering changes, update the top card.
        orderedCards.observe(cards -> {
            if (cards == null || cards.size() == 0) return;
            var card = cards.get(0);
            this.topCard.setValue(card);
        });

        // When the top card changes, update the displayed text and display the front side.
        topCard.observe(card -> {
            if (card == null) return;

            displayedText.setValue(card.front());
            isShowingFront.setValue(true);
        });

        // When isShowingFront changes, update the displayed text.
        isShowingFront.observe(isShowingFront -> {
            if (isShowingFront == null) return;

            var card = topCard.getValue();
            if (card == null) return;

            var text = isShowingFront ? card.front() : card.back();
            displayedText.setValue(text);
        });
    }

    public Subject<String> getDisplayedText() {
        return displayedText;
    }

    public Subject<List<Flashcard>> getOrderedCards() {
        return orderedCards;
    }

    public void flipTopCard() {
        var isShowingFront = this.isShowingFront.getValue();
        if (isShowingFront == null) return;
        this.isShowingFront.setValue(!isShowingFront);
    }

    public void stepForward() {
        var cards = this.orderedCards.getValue();
        if (cards == null) return;

        var newCards = Flashcards.rotate(cards, -1);
        flashcardRepository.save(newCards);
    }

    public void stepBackward() {
        var cards = this.orderedCards.getValue();
        if (cards == null) return;

        var newCards = Flashcards.rotate(cards, 1);
        flashcardRepository.save(newCards);
    }

    public void shuffle() {
        var cards = this.orderedCards.getValue();
        if (cards == null) return;

        var newCards = Flashcards.shuffle(cards);
        flashcardRepository.save(newCards);
    }

    public void append(Flashcard card) {
        flashcardRepository.append(card);
    }

    public void prepend(Flashcard card) {
        flashcardRepository.prepend(card);
    }

    public void remove(int id) {
        flashcardRepository.remove(id);
    }
}
