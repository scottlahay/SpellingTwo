package scott.spelling.system;

import android.content.*;

import java.util.concurrent.*;

import bolts.*;
import scott.spelling.model.*;

import static scott.spelling.system.UrlUtil.*;

public class DataRepo {

    public LocalCache localCache;

    public DataRepo(Context context) { localCache = new LocalCache(OpenHelper.instance(context).getReadableDatabase()); }
    public Task<SpellingLists> syncData() { return Task.callInBackground(new PullListsFromGoogleDrive()); }
    public Task<SpellingLists> getLocalData() { return Task.forResult(localCache.getLists()); }
    public Task<SpellingList> getLocalList(String name) { return Task.forResult(localCache.getList(name)); }

    class PullListsFromGoogleDrive implements Callable<SpellingLists> {
        @Override public SpellingLists call() throws Exception {
            return SpellingLists.createCsv(new FileDownloader().download(KIDS_SPELLING));
        }
    }
}
