package scott.spelling.model;

import org.junit.*;

import static junit.framework.Assert.*;

public class SpellingTest {

    SpellingList list;
    String word1 = "one";
    String word2 = "two";
    String word3 = "three";

    @Before
    public void before() {
        list = SpellingList.create("id", word1, word2);
    }

    @Test
    public void wordsCanBeAddedToASpellingList() {
        assertTrue(list.contains(word1));
        list.add(word3);
        assertTrue(list.contains(word3));
        assertEquals(3, list.size());
    }


    @Test
    public void theListUnderstandsTheCurrentWord() {
        assertEquals(word1, list.currentWord());
    }

    @Test
    public void listsCanMoveThroughTheWords() {
        list.nextWord();
        assertEquals(word2, list.currentWord());
    }

    @Test
    public void listsUnderstandWhenTheyAreAtTheEnd() {
        list.nextWord();
        assertTrue(list.finished());
    }

    @Test
    public void listUnderstandIfTheUserHasEnteredTheCompletedWord() {
        assertTrue(list.isCorrectAnswer(word1));
    }

    @Test
    public void listUnderstandIfTheUserHasNotCompletedWord() {
        assertFalse(list.isCorrectAnswer("w"));
    }

    @Test
    public void listRollOverWhenYouTryAndMovePastTheEndOfTheWord() {
        list.nextWord();
        list.nextWord();
        assertEquals(word1, list.currentWord());
    }
}