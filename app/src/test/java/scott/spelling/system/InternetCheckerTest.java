package scott.spelling.system;

import org.junit.*;

import scott.spelling.system.*;

import static scott.spelling.system.TestUtils.currentKey;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InternetCheckerTest {

    InternetChecker checker;
    LocalCache localCache;

    @Before
    public void before() throws Exception {
        checker = new InternetChecker();
        localCache = mock(LocalCache.class);
        checker.key = currentKey();
    }

    @Test
    public void listsAreOldIfTheyAreEmpty() throws Throwable {
        when(localCache.isEmpty()).thenReturn(true);
        assertTrue(checker.listIsOld(localCache));
    }

    @Test
    public void listAreOldIfTheCachedKeyIsDifferentThanTheServerKey() throws Throwable {
        when(localCache.key()).thenReturn(TestUtils.oldKey());
        assertTrue(checker.listIsOld(localCache));
    }

    @Test
    public void listAreCurrentIfTheCachedKeyIsTheSameAsTheServerKey() throws Throwable {
        when(localCache.key()).thenReturn(currentKey());
        assertFalse(checker.listIsOld(localCache));
    }
}