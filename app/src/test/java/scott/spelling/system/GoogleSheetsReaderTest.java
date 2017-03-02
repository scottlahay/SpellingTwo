package scott.spelling.system;

import android.os.*;

import org.junit.*;
import org.junit.runner.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import java.util.*;

import scott.spelling.model.*;
import scott.spellingtwo.*;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class GoogleSheetsReaderTest {


    private FileDownloader downloader;
    private List<String> csvFile;

    @Before
    public void before() {
        downloader = new FileDownloader();
        csvFile = downloader.download(UrlUtil.TEST_SHEET_URL);
    }

    @Test
    public void ICanGetDataFromASpreadsheet() {
        String actual = csvFile.get(0);
        assertTrue(actual.contains("Test Spelling"));
    }

    @Test
    public void csvDataCanBeConvertedToSpellingLists() {
        SpellingList actual = new SpellingLists(csvFile).findList("Test Spelling one");
        SpellingList expected = SpellingList.create("list_one","one", "two", "three");
        assertEquals(expected, actual);
    }
}