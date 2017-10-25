package scott.spelling.system;

import org.junit.*;

import scott.spelling.model.*;

import static org.robolectric.RuntimeEnvironment.*;


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
//        local.saveSpellingLists(spellingList.getId());
//        assertEquals(spellingList, local.getList(spellingList.getId()));
    }
    private void testSavingWords() {
        local.saveWordsToDb(spellingList.getId(), spellingList.getWords().toArray(new String[0]));
        assertEquals("one", local.getWords(spellingList.getId()).get(0));
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