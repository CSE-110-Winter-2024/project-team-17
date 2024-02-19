package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Flashcard;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Class used as a sort of "database" of decks and flashcards that exist. This
 * will be replaced with a real database in the future, but can also be used
 * for testing.
 */
public class InMemoryDataSource {
    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;

    private final Map<Integer, Flashcard> flashcards
            = new HashMap<>();
    private final Map<Integer, MutableSubject<Flashcard>> flashcardSubjects
            = new HashMap<>();
    private final MutableSubject<List<Flashcard>> allFlashcardsSubject
            = new SimpleSubject<>();

    public InMemoryDataSource() {
    }

    public final static List<Flashcard> DEFAULT_CARDS = List.of(
        /*new Flashcard(0, "Dishes", "Single Responsibility Principle", 0),
        new Flashcard(1, "Dinner", "Open-Closed Principle", 1),
        new Flashcard(2, "Laundry", "Liskov Substitution Principle", 2),
        new Flashcard(3, "Study 140", "Interface Segregation Principle", 3),
        new Flashcard(4, "Study 110", "Dependency Inversion Principle", 4),
        new Flashcard(5, "Do Lab", "Least Knowledge Principle (Law of Demeter)", 5)*/
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.putFlashcards(DEFAULT_CARDS);
        return data;
    }

    public List<Flashcard> getFlashcards() {
        return List.copyOf(flashcards.values());
    }

    public Flashcard getFlashcard(int id) {
        return flashcards.get(id);
    }

    public Subject<Flashcard> getFlashcardSubject(int id) {
        if (!flashcardSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Flashcard>();
            subject.setValue(getFlashcard(id));
            flashcardSubjects.put(id, subject);
        }
        return flashcardSubjects.get(id);
    }

    public Subject<List<Flashcard>> getAllFlashcardsSubject() {
        return allFlashcardsSubject;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    public void putFlashcard(Flashcard card) {
        var fixedCard = preInsert(card);

        flashcards.put(fixedCard.id(), fixedCard);
        postInsert();
        assertSortOrderConstraints();

        if (flashcardSubjects.containsKey(fixedCard.id())) {
            flashcardSubjects.get(fixedCard.id()).setValue(fixedCard);
        }
        allFlashcardsSubject.setValue(getFlashcards());
    }

    public void putFlashcards(List<Flashcard> cards) {
        var fixedCards = cards.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedCards.forEach(card -> flashcards.put(card.id(), card));
        postInsert();
        assertSortOrderConstraints();

        fixedCards.forEach(card -> {
            if (flashcardSubjects.containsKey(card.id())) {
                flashcardSubjects.get(card.id()).setValue(card);
            }
        });
        allFlashcardsSubject.setValue(getFlashcards());
    }

    public void removeFlashcard(int id) {
        var card = flashcards.get(id);
        var sortOrder = card.sortOrder();

        flashcards.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (flashcardSubjects.containsKey(id)) {
            flashcardSubjects.get(id).setValue(null);
        }
        allFlashcardsSubject.setValue(getFlashcards());
    }

    public void shiftSortOrders(int from, int to, int by) {
        var cards = flashcards.values().stream()
                .filter(card -> card.sortOrder() >= from && card.sortOrder() <= to)
                .map(card -> card.withSortOrder(card.sortOrder() + by))
                .collect(Collectors.toList());

        putFlashcards(cards);
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * cards inserted have an id, and updates the nextId if necessary.
     */
    private Flashcard preInsert(Flashcard card) {
        var id = card.id();
        if (id == null) {
            // If the card has no id, give it one.
            card = card.withId(nextId++);
        }
        else if (id > nextId) {
            // If the card has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load cards like in fromDefault().
            nextId = id + 1;
        }

        return card;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = flashcards.values().stream()
                .map(Flashcard::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = flashcards.values().stream()
                .map(Flashcard::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * <p></p>
     * Will throw an AssertionError if any of the constraints are violated,
     * which should never happen. Mostly here to make sure I (Dylan) don't
     * write incorrect code by accident!
     */
    private void assertSortOrderConstraints() {
        // Get all the sort orders...
        var sortOrders = flashcards.values().stream()
                .map(Flashcard::sortOrder)
                .collect(Collectors.toList());

        // Non-negative...
        assert sortOrders.stream().allMatch(i -> i >= 0);

        // Unique...
        assert sortOrders.size() == sortOrders.stream().distinct().count();

        // Between min and max...
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }
}

