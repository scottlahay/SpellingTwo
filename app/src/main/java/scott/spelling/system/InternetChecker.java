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

    public boolean listIsOld(LocalCache localCache) { return localCache.isEmpty() || !getKey().equals(localCache.key()); }

    public boolean isNetworkAvailable() { // todo for this code to work in the emulator, it looks like I need to do some work.
        // todo should also check internet connectivity
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnected(); // todo bring this back
        return false;
    }
    private String getKey() {return key;} // yes this will have to connect to google sheets
    public Task<Boolean> availability() { return Task.call(new RunCheck()); }

    class RunCheck implements Callable<Boolean> {
        @Override public Boolean call() throws Exception {
            return isNetworkAvailable();
        }
    }
}
