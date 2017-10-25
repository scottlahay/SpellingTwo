package scott.spelling.system;

import android.os.*;

import org.junit.*;
import org.junit.runner.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import java.util.*;

import scott.spelling.*;

import static org.junit.Assert.*;
import static org.robolectric.RuntimeEnvironment.*;
import static scott.spelling.system.UrlUtil.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class GoogleSheetsReaderTest {

    public boolean isNetworkAvailable;
    private List<String> csvFile;
    @Before
    public void before() {
        FileDownloader downloader = new FileDownloader();
        csvFile = downloader.download(TEST_SHEET_URL);
//     TODO   this code doesnt work as expected in the test, needs a little more testing.
//  Robolectric probably is faking some stuff that makes this work differently that a live device so looking
//        into isNetworkAvailable stuff (When I can actually connect to the internet)
        isNetworkAvailable = new InternetChecker(application).isNetworkAvailable();
    }

    @Test  // relies on a network connections, So If I don't have a a connection don't fail it
    public void ICanGetDataFromASpreadsheet() {
        if (isNetworkAvailable) {
            assertTrue(csvFile.get(0).contains("Test Spelling"));
        }
    }

}