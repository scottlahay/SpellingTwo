package scott.spelling.system;

import junit.framework.Assert;

import org.junit.Test;

import scott.spelling.system.*;

public class UrlUtilTest {

    @Test
    public void urlsCanBeBuiltCorrectly() {
        String actual = UrlUtil.buildUrl(UrlUtil.TEST_SHEET_URL);
        String expected = "https://docs.google.com/spreadsheets/d/1kNpXs13m0nvVZDK71ab4yc8RE5Yv-dMeZ_-gt02gue0/export?format=csv";
        Assert.assertEquals(expected, actual);
    }
}