package scott.spelling.presenter;

import scott.spelling.system.*;

public class SpellingPresenter {
    SpellingView view;
    InternetChecker internet;
    DataRepo dataRepo;

    public SpellingPresenter(SpellingView view, InternetChecker internet, DataRepo dataRepo) {
        this.view = view;
        this.internet = internet;
        this.dataRepo = dataRepo;
    }

    public void init() {
        if (!internet.available()) {
            if (dataRepo.hasLocalData()) { view.displayLists(); }
            else { view.displayHelpPage(); }
        }
        else {
            view.showProgress();
            dataRepo.synchData();
        }
    }

    public void showEmptyListSplashPage() { }
    public void displayListChooser() { }
    public void showLists() {
        view.hideProgress();
        view.displayLists();
    }

    public interface SpellingView {
        void displayHelpPage();
        void displayLists();
        void showProgress();
        void hideProgress();
    }
}
