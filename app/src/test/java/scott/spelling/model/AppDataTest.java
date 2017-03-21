package scott.spelling.model;

import org.junit.*;

import scott.spelling.system.*;

import static org.junit.Assert.*;

public class AppDataTest {

    AppData appData;

    @Before
    public void before() {
        appData = TestUtils.appData();
    }

    @Test
    public void appDataIsOutdatedIfAnyOfTheFieldsAreMissingData() throws Throwable {
        assertFalse(appData.outdated());
        boolean outdated = AppData.create("", "").outdated();
        assertTrue(outdated);
    }
}