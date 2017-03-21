package scott.spelling.system;

import android.content.*;
import android.util.*;

import java.util.*;
import java.util.concurrent.*;

import bolts.*;
import scott.spelling.model.*;

import static scott.spelling.system.UrlUtil.*;
import static scott.spelling.view.SpellingActivity.*;

public class DataRepo {

    public LocalCache localCache;

    public DataRepo(LocalCache localCache) { this.localCache = localCache;}
    public DataRepo(Context context) { this(new LocalCache(OpenHelper.instance(context).getWritableDatabase())); }

    public Task<SpellingLists> getLocalLists() { return Task.forResult(localCache.getLists()); }
    public Task<SpellingList> getLocalList(String name) { return Task.forResult(localCache.getList(name)); }
    public Task<AppData> getAppData() { return Task.forResult(localCache.initCheck()).continueWithTask(new GetAppData()); }
    public void updateAppData(AppData appData) { Task.callInBackground(new UpdateAppData(appData)); }

    public Task<SpellingLists> syncDataCheck() {
        AppData appData = getAppData().getResult();
        if (appData == null || appData.outdated()) {return Task.callInBackground(new PullListsFromGoogleDrive()).continueWith(new ProcessServerLists()); }
        return null;
    }

    class PullListsFromGoogleDrive implements Callable<List<String>> {
        @Override public List<String> call() throws Exception {
            return new FileDownloader().download(KIDS_SPELLING);
        }
    }

    class ProcessServerLists implements Continuation<List<String>, SpellingLists> {
        @Override public SpellingLists then(Task<List<String>> task) throws Exception {
            SpellingLists lists = SpellingLists.createCsv(task.getResult());
            localCache.replaceAllLists(lists);
            return lists;
        }
    }

    class UpdateAppData implements Callable<Object> {
        private final AppData appData;
        public UpdateAppData(AppData appData) {this.appData = appData;}
        @Override public Object call() throws Exception {
            localCache.saveAppData(appData);
            return null;
        }
    }

    class GetAppData implements Continuation<Void, Task<AppData>> {
        @Override public Task<AppData> then(Task<Void> task) throws Exception {
            Log.d(TAG_IT, "then: DataRepo.GetAppData");
            return Task.forResult(localCache.getAppData());
        }
    }
}
