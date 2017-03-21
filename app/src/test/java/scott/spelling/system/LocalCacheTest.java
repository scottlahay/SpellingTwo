package scott.spelling.system;

import org.junit.*;
import org.junit.runner.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import scott.spelling.model.*;

import static org.junit.Assert.*;
import static org.robolectric.RuntimeEnvironment.*;
import static scott.spelling.system.TestUtils.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LocalCacheTest {

    LocalCache local;
    SpellingList spellingList;

    @Test  // this test is weird in that the database seems to have issues when I run multiple units tests,
    // So I'm taking the quick and sleazy solution here by just putting it all into one unit test
    public void spellingListsCanBeAddedAndRemovedFromTheDatabase() throws Throwable {
        local = new LocalCache(OpenHelper.instance(application).getWritableDatabase()).createTables();
        spellingList = TestUtils.spellingListWeekOne();

        testSaveAppData();
        testSavingWords();
        testSaveSpellingList();
    }
    private void testSaveSpellingList() {
        local.saveSpellingLists(spellingList.id());
        assertEquals(spellingList, local.getList(spellingList.id()));
    }
    private void testSavingWords() {
        local.saveWordsToDb(spellingList.id(), spellingList.words().toArray(new String[0]));
        assertEquals("one", local.getWords(spellingList.id()).get(0));
    }
    private void testSaveAppData() {
        AppData appData = appData();
        local.saveAppData(appData);
        assertEquals(appData, local.getAppData());
// we replace rather than update the row
        AppData appData2 = appData2();
        local.saveAppData(appData2);
        assertEquals(appData2, local.getAppData());

    }


}