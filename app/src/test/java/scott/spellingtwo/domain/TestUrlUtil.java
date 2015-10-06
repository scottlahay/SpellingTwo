package scott.spellingtwo.domain;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import scott.spellingtwo.BuildConfig;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestUrlUtil {

    @Test
    public void urlsCanBeBuiltCorrectly() {
        String actual = UrlUtil.buildUrl(UrlUtil.TEST_SHEET_URL);
        String expected = "https://docs.google.com/spreadsheets/d/1kNpXs13m0nvVZDK71ab4yc8RE5Yv-dMeZ_-gt02gue0/export?format=csv";
        Assert.assertEquals(expected, actual);
    }
}