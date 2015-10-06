package scott.spellingtwo.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import scott.spellingtwo.BuildConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestGoogleSheetsReader {


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
        SpellingList expected = new SpellingList("one", "two", "three");
        assertEquals(expected, actual);
    }
}