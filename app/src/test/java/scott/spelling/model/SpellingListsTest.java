package scott.spelling.model;

import org.junit.*;

import scott.spelling.system.*;

import static org.junit.Assert.*;
import static scott.spelling.system.TestUtils.*;

public class SpellingListsTest {

    SpellingLists lists;

    @Before
    public void before() {
        lists = TestUtils.spellingLists();
    }

    @Test
    public void spellingListsAreEmptyWhenTheDontContainAList() throws Throwable {
        assertTrue(emptySpellingLists().empty());
        assertFalse(spellingLists().empty());
    }

    @Test
    public void aListCanBeFoundByItsIndexNumber() throws Throwable {
        assertEquals(spellingListWeekOne(), lists.findList(0));
    }
}