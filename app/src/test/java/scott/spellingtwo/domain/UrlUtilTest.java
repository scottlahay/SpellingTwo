package scott.spellingtwo.domain;

import android.os.*;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import scott.spellingtwo.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class UrlUtilTest {

    @Test
    public void urlsCanBeBuiltCorrectly() {
        String actual = UrlUtil.buildUrl(UrlUtil.TEST_SHEET_URL);
        String expected = "https://docs.google.com/spreadsheets/d/1kNpXs13m0nvVZDK71ab4yc8RE5Yv-dMeZ_-gt02gue0/export?format=csv";
        Assert.assertEquals(expected, actual);
    }
}