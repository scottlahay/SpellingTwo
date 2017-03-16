package scott.spelling;

import android.support.test.espresso.*;

import com.mauriciotogneri.greencoffee.*;
import com.mauriciotogneri.greencoffee.annotations.*;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

class SpellingSteps extends GreenCoffeeSteps {

    @Given("^A set of spelling lists$")
    public void showsTheList() throws Throwable {
        Thread.sleep(5000);
    }

    @When("^Will chooses (.+)$")
    public void choosesList(String listName) throws Throwable {
        onView(withText(listName)).perform(click());
    }

    @Then("^Will sees a hint with (.+) as the next word$")
    public void seesHint(String word) throws Throwable {
        Thread.sleep(5000);
        try {
            onView(withText(word)).perform(click());
            // View is in hierarchy

        } catch (NoMatchingViewException e) {
            fail();
        }
    }
}
