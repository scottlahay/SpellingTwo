package scott.spelling.system;

import android.content.*;

import java.util.concurrent.*;

import bolts.*;
import scott.spelling.model.*;

import static scott.spelling.system.UrlUtil.*;

public class DataRepo {

    public LocalCache localCache;
    private Callable<SpellingLists> getSpellingLists = new Callable<SpellingLists>() {
        @Override public SpellingLists call() throws Exception {
            return SpellingLists.createCsv(new FileDownloader().download(KIDS_SPELLING));
        }
    };

    public DataRepo(Context context) { localCache = new LocalCache(OpenHelper.instance(context).getReadableDatabase()); }
    public Task<SpellingLists> synchData() { return Task.callInBackground(getSpellingLists); }
    public Task<SpellingLists> getLocalData() { return Task.forResult(localCache.getLists()); }
}
