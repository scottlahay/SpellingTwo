package scott.spelling.system;

import android.content.*;

import java.util.concurrent.*;

import bolts.*;
import scott.spelling.model.*;

import static scott.spelling.system.UrlUtil.*;

public class DataRepo {

    private Callable<SpellingLists> getSpellingLists = new Callable<SpellingLists>() {
        @Override public SpellingLists call() throws Exception {
            return SpellingLists.createCsv(new FileDownloader().download(KIDS_SPELLING));
        }
    };

    public void load() { }

    public Task<SpellingLists> synchData(Context context) {
        return Task.callInBackground(getSpellingLists);
    }

    public boolean hasLocalData() {return false;}

    public Task<SpellingLists> getLocalData(Context context) {
        LocalCache localCache = new LocalCache(OpenHelper.instance(context).getReadableDatabase());
        return Task.forResult(localCache.getLists());
    }
}
