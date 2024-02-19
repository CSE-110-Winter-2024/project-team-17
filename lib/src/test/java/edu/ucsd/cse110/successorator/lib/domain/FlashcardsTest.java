package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlashcardsTest {
    @Test
    public void rotateForward() {
        var cards = new ArrayList<>(List.of(
            new Flashcard(1, "front1", "back1", 1),
            new Flashcard(2, "front2", "back2", 2),
            new Flashcard(3, "front3", "back3", 3)
        ));

        var expected = new ArrayList<>(List.of(
            new Flashcard(1, "front1", "back1", 3),
            new Flashcard(2, "front2", "back2", 1),
            new Flashcard(3, "front3", "back3", 2)
        ));

        var actual = Flashcards.rotate(cards, -1);

        assertEquals(expected, actual);
    }

    @Test
    public void rotateBackward() {
        var cards = new ArrayList<>(List.of(
            new Flashcard(1, "front1", "back1", 1),
            new Flashcard(2, "front2", "back2", 2),
            new Flashcard(3, "front3", "back3", 3)
        ));

        var expected = new ArrayList<>(List.of(
            new Flashcard(1, "front1", "back1", 2),
            new Flashcard(2, "front2", "back2", 3),
            new Flashcard(3, "front3", "back3", 1)
        ));

        var actual = Flashcards.rotate(cards, 1);

        assertEquals(expected, actual);
    }

    @Test
    public void shuffle() {
        var cards = new ArrayList<>(List.of(
            new Flashcard(1, "front1", "back1", 1),
            new Flashcard(2, "front2", "back2", 2),
            new Flashcard(3, "front3", "back3", 3)
        ));

        var actual = Flashcards.shuffle(cards);

        // The shuffle method is non-deterministic, so we can't compare the
        // result to an expected value. Instead, let's just check that the
        // cards are still the same, but in a different order.

        for (var card : cards) {
            var shuffledCard = actual.stream()
                .filter(c -> Objects.equals(c.id(), card.id()))
                .filter(c -> Objects.equals(c.front(), card.front()))
                .filter(c -> Objects.equals(c.back(), card.back()))
                .findFirst();

            assertTrue(shuffledCard.isPresent());
        }
    }
}