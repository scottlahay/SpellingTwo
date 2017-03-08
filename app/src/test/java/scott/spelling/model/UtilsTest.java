package scott.spelling.model;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;
import static scott.spelling.model.Utils.*;

public class UtilsTest {

    @Before
    public void before() {
    }

    @Test
    public void understandsWhenAHashMapIsEmpty() throws Throwable {
        HashMap map = new HashMap();
        map.put("key", "value");
        assertFalse(isNullOrEmpty(map));
        map.clear();
        assertTrue(isNullOrEmpty(map));
    }

    @Test
    public void understandsWhenAListIsEmpty() throws Throwable {
        List<String> list = asList("One");
        assertFalse(isNullOrEmpty(list));
        list.clear();
        assertTrue(isNullOrEmpty(list));
    }

    @Test
    public void understandsWhatTheLastItemInAListIs() throws Throwable {

        assertNull(lastInList(null));
        assertNull(lastInList(new ArrayList<>()));
        List<String> list = asList("one");
        assertEquals("one", lastInList(list));
        list.add("two");
        assertEquals("two", lastInList(list));
    }
}