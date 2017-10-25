package scott.spelling;

import android.support.test.rule.*;

import com.mauriciotogneri.greencoffee.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.*;
import java.util.*;

import scott.spelling.view.*;

@RunWith(Parameterized.class)
public class SpellingUiTest extends GreenCoffeeTest {
    @Rule
    public ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

    public SpellingUiTest(ScenarioConfig scenarioConfig) {
        super(scenarioConfig);
    }
    @Parameterized.Parameters
    public static Iterable<ScenarioConfig> scenarios() throws IOException {
        return new GreenCoffeeConfig("testapp") // folder to place the screenshot if a test fails
                                                .withFeatureFromAssets("assets/features/checkmark.feature")
                                                .scenarios();
    }
    @Override
    protected void beforeScenarioStarts(Scenario scenario, Locale locale) {
    }
    @Test
    public void test() {
        start(new SpellingSteps());
    }
}
