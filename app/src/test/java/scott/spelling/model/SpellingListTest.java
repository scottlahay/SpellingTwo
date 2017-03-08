package scott.spelling.model;

import org.junit.*;

import static org.junit.Assert.*;
import static scott.spelling.system.TestUtils.*;

public class SpellingListTest {

    SpellingList list;

    @Before
    public void before() {
        list = spellingList();
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
}