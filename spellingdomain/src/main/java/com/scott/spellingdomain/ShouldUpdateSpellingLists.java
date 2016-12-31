package com.scott.spellingdomain;

public class ShouldUpdateSpellingLists {
    public void updateLists(SpellingPresenter presenter, InternetChecker internetChecker, LocalCache localCache) {
        updateTheListIfNecessary(internetChecker, localCache);
        directTheUser(presenter, localCache);
    }

    void directTheUser(SpellingPresenter presenter, LocalCache localCache) {
        if (localCache.isEmpty()) {
            presenter.showEmptyListSplashPage();
        }
        else {
            presenter.displayListChooser();
        }
    }
    void updateTheListIfNecessary(InternetChecker internetChecker, LocalCache localCache) {
        if (internetChecker.hasInternet()) {
            if (internetChecker.listIsOld(localCache) || localCache.isEmpty()) {
                localCache.setList(internetChecker.getLists());
            }
        }
    }
}
