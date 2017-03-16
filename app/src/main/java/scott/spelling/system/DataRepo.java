package scott.spelling.system;

import android.content.*;

import java.util.*;
import java.util.concurrent.*;

import bolts.*;
import scott.spelling.model.*;

import static scott.spelling.system.UrlUtil.*;

public class DataRepo {

    public LocalCache localCache;

    public DataRepo(Context context) {
        localCache = new LocalCache(OpenHelper.instance(context).getWritableDatabase());
    }
    public Task<SpellingLists> syncData() { return Task.callInBackground(new PullListsFromGoogleDrive()).continueWith(new ProcessServerLists()); }
    public Task<SpellingLists> getLocalData() { return Task.forResult(localCache.getLists()); }
    public Task<SpellingList> getLocalList(String name) { return Task.forResult(localCache.getList(name)); }

    class PullListsFromGoogleDrive implements Callable<List<String>> {
        @Override public List<String> call() throws Exception {
            return new FileDownloader().download(KIDS_SPELLING);
        }
    }

    //    @TODO test me!
    class ProcessServerLists implements Continuation<List<String>, SpellingLists> {
        @Override public SpellingLists then(Task<List<String>> task) throws Exception {
            SpellingLists lists = SpellingLists.createCsv(task.getResult());
            localCache.replaceAllLists(lists);
            return lists;
        }
    }

}
