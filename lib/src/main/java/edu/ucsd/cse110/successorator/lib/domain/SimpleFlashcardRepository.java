package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleFlashcardRepository implements FlashcardRepository {
    private final InMemoryDataSource dataSource;

    public SimpleFlashcardRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Flashcard> find(int id) {
        return dataSource.getFlashcardSubject(id);
    }

    @Override
    public Subject<List<Flashcard>> findAll() {
        return dataSource.getAllFlashcardsSubject();
    }

    @Override
    public void save(Flashcard flashcard) {
        dataSource.putFlashcard(flashcard);
    }

    @Override
    public void save(List<Flashcard> flashcards) {
        dataSource.putFlashcards(flashcards);
    }

    @Override
    public void remove(int id) {
        dataSource.removeFlashcard(id);
    }

    @Override
    public void append(Flashcard flashcard) {
        dataSource.putFlashcard(
                flashcard.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    @Override
    public void prepend(Flashcard flashcard) {
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putFlashcard(
                flashcard.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }
}
