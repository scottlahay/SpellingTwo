package scott.spelling.model;

import org.junit.*;

import static org.junit.Assert.*;
import static scott.spelling.system.TestUtils.*;

public class SpellingListTest {

    SpellingList list;

    @Before
    public void before() {
        list = spellingListWeekOne();
    }

    @Test
    public void theListCanBeFastForwardedToTheEnd() throws Throwable {
        list.setAtEnd();
        assertTrue(list.atEnd());
    }

    @Test
    public void theListUnderstandsWhenItsAtTheStart() throws Throwable {
        assertTrue(list.atStart());
        list.nextWord();
        assertFalse(list.atStart());
    }

    @Test
    public void theListCanFindThereCompletionPercentages() throws Throwable {

        assertEquals(0, list.primaryProgress());
        list.nextWord();
        assertEquals(33, list.primaryProgress());
        list.nextWord();
        assertEquals(66, list.primaryProgress());
    }
}