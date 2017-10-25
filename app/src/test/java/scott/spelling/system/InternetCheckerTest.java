package scott.spelling.system;

import org.junit.*;

import static org.mockito.Mockito.*;
import static org.robolectric.RuntimeEnvironment.*;

public class InternetCheckerTest {

    InternetChecker checker;
    LocalCache localCache;

    @Before
    public void before() throws Exception {
        checker = new InternetChecker(application);
        localCache = mock(LocalCache.class);
        checker.key = currentKey();
    }

//    @Test
//    public void listsAreOldIfTheyAreEmpty() throws Throwable {
//        when(localCache.isEmpty()).thenReturn(true);
//        assertTrue(checker.listIsOld(localCache));
//    }
//
//    @Test
//    public void listAreOldIfTheCachedKeyIsDifferentThanTheServerKey() throws Throwable {
//        when(localCache.key()).thenReturn(TestUtils.oldKey());
//        assertTrue(checker.listIsOld(localCache));
//    }
//
//    @Test
//    public void listAreCurrentIfTheCachedKeyIsTheSameAsTheServerKey() throws Throwable {
//        when(localCache.key()).thenReturn(currentKey());
//        assertFalse(checker.listIsOld(localCache));
//    }
}