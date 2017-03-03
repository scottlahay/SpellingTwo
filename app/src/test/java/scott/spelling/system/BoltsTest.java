package scott.spelling.system;

import org.junit.*;

import java.util.concurrent.*;

import bolts.*;

import static org.junit.Assert.assertTrue;

public class BoltsTest {

    public Callable<Boolean> callable = new Callable<Boolean>() {
        @Override public Boolean call() throws Exception {
            Thread.sleep(100);
            return true;
        }
    };

    @Test
    public void aSimpleBoltsUnitTestToShowHowItsDone() throws Throwable {
        Task<Boolean> task = Task.callInBackground(callable);
        task.waitForCompletion();
        assertTrue(task.getResult());
    }
}