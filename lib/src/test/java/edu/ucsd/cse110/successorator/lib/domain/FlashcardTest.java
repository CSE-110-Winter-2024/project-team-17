package edu.ucsd.cse110.successorator.lib.domain;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;

import org.junit.Test;

public class FlashcardTest {
    @Test
    public void testGetters() {
        var card = new Flashcard(1, "front", "back", 0);
        assertEquals(Integer.valueOf(1), card.id());
        assertEquals("front", card.front());
        assertEquals("back", card.back());
        assertEquals(0, card.sortOrder());
    }

    @Test
    public void testWithId() {
        var card = new Flashcard(1, "front", "back", 0);
        var expected = new Flashcard(42, "front", "back", 0);
        var actual = card.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var card = new Flashcard(1, "front", "back", 0);
        var expected = new Flashcard(1, "front", "back", 42);
        var actual = card.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var card1 = new Flashcard(1, "front", "back", 0);
        var card2 = new Flashcard(1, "front", "back", 0);
        var card3 = new Flashcard(2, "front", "back", 0);

        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }
}