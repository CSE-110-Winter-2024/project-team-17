package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Flashcards {

    public static List<Flashcard> rotate(List<Flashcard> cards, int k) {
        var newCards = new ArrayList<Flashcard>();
        for (int i = 0; i < cards.size(); i++) {
            var thisCard = cards.get(i);
            var thatCard = cards.get(Math.floorMod(i + k, cards.size()));
            newCards.add(thisCard.withSortOrder(thatCard.sortOrder()));
        }
        return newCards;
    }

    public static List<Flashcard> reorder(List<Flashcard> cards, int id, int index) {
        // Find the flashcard with the given id
        Flashcard flashcardToReorder = null;
        int currentIndex = -1;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).id() == id) {
                flashcardToReorder = cards.get(i);
                currentIndex = i;
                break;
            }
        }

        // If flashcard with given id not found, return original list
        if (flashcardToReorder == null) {
            return cards;
        }

        // Remove the flashcard from its current position
        cards.remove(currentIndex);

        // Adjust index if it exceeds the size of the list
        if (index > cards.size()) {
            index = cards.size();
        }

        // Insert the flashcard at the new position
        cards.add(index, flashcardToReorder);

        return cards;
    }


    @NonNull
    public static List<Flashcard> shuffle(List<Flashcard> cards) {
        var sortOrders = cards.stream()
                .map(Flashcard::sortOrder)
                .collect(Collectors.toList());

        Collections.shuffle(sortOrders);
        return cards.stream()
                .map(card -> card.withSortOrder(sortOrders.remove(0)))
                .collect(Collectors.toList());
    }

}
