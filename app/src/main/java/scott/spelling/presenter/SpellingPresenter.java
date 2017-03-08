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
    SpellingList spellingList;
    String answerText = "";

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
        view.popUpChooseList(spellingLists.listNames());
    }

    private void noData() {
        view.hideProgress();
        view.showPopUp("Sorry, the first time you use this app you need to have an internet connection. Please connect to the Internet", "");
    }

    public void userSelectsThereList(int listIndex) {
        spellingList = spellingLists.findList(listIndex);
        dataRepo.getLocalList(spellingList.id()).continueWith(new ShowFirstWord());
    }

    public void updateHeader(SpellingList spellingList) {
        view.updateHeader(spellingList.current + "/" + spellingList.size());
    }

    public void showHint() {
        view.speak(spellingList.currentWord());
        view.showPopUp("The word is", spellingList.currentWord());
    }

    public void updateText(String answerText) {
        view.setTheAnswer(answerText);
        updateHeader(spellingList);
    }

    public void keyPressed(char unicodeChar) {
        answerText += Character.toString(unicodeChar);
        if (spellingList.isCorrectAnswer(answerText)) {
            answerText = "";
            if (spellingList.atEnd()) { view.popUpYouFinished(); }
            else {
                spellingList.nextWord();
                showHint();
            }
        }
        updateText(answerText);
    }

    public void startOver() {
        spellingList.setAtStart();
        updateText(answerText);
        showHint();
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
            (task.getResult() ? dataRepo.syncData() : dataRepo.getLocalData()).onSuccess(new ShowLists());
            return null;
        }
    }

    class ShowFirstWord implements Continuation<SpellingList, Void> {
        @Override public Void then(Task<SpellingList> task) throws Exception {
            view.showPopUp("Let's get started with ", task.getResult().currentWord());
            return null;
        }
    }
}
