package scott.spelling.system;

import android.content.*;
import android.net.*;

import java.util.concurrent.*;

import bolts.*;

public class InternetChecker {
    String key;
    Context context;

    public Task<Boolean> availability() {
        return Task.call(new RunCheck());
    }
    public boolean listIsOld(LocalCache localCache) {
        if (localCache.isEmpty()) { return true; }
        else { return !getKey().equals(localCache.key()); }
    }

    private String getKey() {return key;} // yes this will have to connect to google sheets


    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class RunCheck implements Callable<Boolean> {
        @Override public Boolean call() throws Exception {
            return isNetworkAvailable();
        }
    }
}
