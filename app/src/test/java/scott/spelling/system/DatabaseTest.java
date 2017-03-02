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
public class DatabaseTest {

    final String key = "list_one";
    LocalCache local;
    SQLiteDatabase db;

    String[] wordlist = new String[]{"one", "two", "three"};
    @Before
    public void before() {
        db = OpenHelper.instance(application).getWritableDatabase();
        local = new LocalCache(db);
    }

    @Test
    public void SpellingListsCanBeAddedAndRemovedFromTheDatabase() throws Throwable {
        local.createWordTable();
        local.createSpellingListTable();

        local.saveWordsToDb(key, wordlist);
        local.saveSpellingLists(key);

        assertEquals("one", local.getWords(key).get(0));
        SpellingList expected = SpellingList.create(key, wordlist);
        assertEquals(expected, local.getLists().get(0));
        assertEquals(expected, local.getList(key));
    }
}