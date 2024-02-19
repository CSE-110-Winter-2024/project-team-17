package edu.ucsd.cse110.secards.lib.domain;

import java.util.List;

import edu.ucsd.cse110.secards.lib.util.Subject;

public interface FlashcardRepository {
    Subject<Flashcard> find(int id);

    Subject<List<Flashcard>> findAll();

    void save(Flashcard flashcard);

    void save(List<Flashcard> flashcards);

    void remove(int id);

    void append(Flashcard flashcard);

    void prepend(Flashcard flashcard);
}
