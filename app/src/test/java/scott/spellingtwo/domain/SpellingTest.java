package scott.spellingtwo.domain;

import android.os.*;

import org.junit.*;
import org.junit.runner.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import scott.spellingtwo.*;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class SpellingTest {

    SpellingList list;
    private String word1 = "one";
    private String word2 = "two";
    private String word3 = "three";

    @Before
    public void before() {
        list = new SpellingList(word1, word2);
    }

    @Test
    public void wordsCanBeAddedToASpellingList() {
        assertTrue(list.contains(word1));
        list.add(word3);
        assertTrue(list.contains(word3));
        assertEquals(3, list.size());
    }

    public void spellingListsCanBeCombined() {
        SpellingList temp = new SpellingList(list);
        assertTrue(temp.contains(word1));
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