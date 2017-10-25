package scott.spelling.system;

import android.content.*;
import android.net.*;

import java.util.concurrent.*;

import bolts.*;

import static android.content.Context.*;

public class InternetChecker {
    String key;
    Context context;

    public InternetChecker(Context context) { this.context = context; }

    public boolean isNetworkAvailable() { // TODO for this code to work in the emulator, it looks like I need to do some work.
        // TODO should also check internet connectivity
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean testing = false;
        return !testing && netInfo != null && netInfo.isConnected();
    }

    private String getKey() {return key;} // yes this will have to connect to google sheets
    public Task<Boolean> availability() { return Task.callInBackground(new RunCheck()); }

    class RunCheck implements Callable<Boolean> {
        @Override public Boolean call() throws Exception {
            return isNetworkAvailable();
        }
    }
}
