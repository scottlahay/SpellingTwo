package scott.spelling.presenter;

import bolts.*;
import scott.spelling.model.*;
import scott.spelling.system.*;
import scott.spelling.view.*;

public class SpellingPresenter {
    SpellingActivity view;
    InternetChecker internet;
    DataRepo dataRepo;
    SpellingLists spellingLists;

    public SpellingPresenter(SpellingActivity view, InternetChecker internet, DataRepo dataRepo) {
        this.view = view;
        this.internet = internet;
        this.dataRepo = dataRepo;
    }

    public void init() {
        view.showProgress();
        internet.availability().continueWith(new ProcessInternetAvailability());
    }

    public void showLists() {
        view.hideProgress();
        view.displayLists(spellingLists);
    }
    private void noData() {
        view.hideProgress();
        view.popUpNoListsOrInternetConnection();
    }

    class ShowLists implements Continuation<SpellingLists, Object> {
        @Override public Object then(Task<SpellingLists> task) throws Exception {
            spellingLists = task.getResult();
            if (spellingLists.empty()) { noData(); }
            else { showLists(); }
            return null;
        }
    }

    class ProcessInternetAvailability implements Continuation<Boolean, Void> {
        @Override public Void then(Task<Boolean> task) throws Exception {
            (task.getResult() ? dataRepo.synchData() : dataRepo.getLocalData()).onSuccess(new ShowLists());
            return null;
        }
    }
}
