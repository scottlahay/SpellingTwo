package scott.spelling.system;

import android.database.sqlite.*;

import org.junit.*;
import org.junit.runner.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import scott.spelling.model.*;

import static org.junit.Assert.*;
import static org.robolectric.RuntimeEnvironment.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LocalCacheTest {

    final String key = "list_one";
    LocalCache local;
    SQLiteDatabase db;

    String[] wordList = new String[]{"one", "two", "three"};

    @Test  // this test is weird in that the database seems to have issues when I run multiple units tests,
    // So I'm taking the quick and sleazy solution here by just putting it all into one unit test
    public void spellingListsCanBeAddedAndRemovedFromTheDatabase() throws Throwable {
        db = OpenHelper.instance(application).getWritableDatabase();
        local = new LocalCache(db);

        local.createTables();

        local.saveWordsToDb(key, wordList);
        local.saveSpellingLists(key);
        String syncKey = "my_sync_key";
        local.saveSyncKey(syncKey);

        assertEquals(syncKey, local.getSyncKey());
        assertEquals("one", local.getWords(key).get(0));
        SpellingList expected = SpellingList.create(key, wordList);
        assertEquals(expected, local.getList(key));
    }

    @Test
    public void appUnderstandsWhenTheDatabaseIsEmpty() throws Throwable {
        fail();
    }

    @Test
    public void keyShouldHaveAValueFromTheDatabase() throws Throwable {
        fail();

    }
}