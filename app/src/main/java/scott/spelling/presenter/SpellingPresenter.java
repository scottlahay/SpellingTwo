package scott.spelling.presenter;

import bolts.*;
import scott.spelling.model.*;
import scott.spelling.system.*;
import scott.spelling.view.*;

import static bolts.Task.*;

public class SpellingPresenter {

    public static final char CAPS_KEY = (char) -1;
    public static final char CLEAR_KEY = (char) -4;
    public static final char HINT_KEY = (char) -5;
    SpellingActivity view;
    InternetChecker internet;
    DataRepo dataRepo;
    SpellingLists spellingLists;
    SpellingList spellingList;
    String answerText = "";
    boolean setAsCap;

    public SpellingPresenter(SpellingActivity view, InternetChecker internet, DataRepo dataRepo) {
        this.view = view;
        this.internet = internet;
        this.dataRepo = dataRepo;
    }

    public void init() {
        view.showProgress();
        internet.availability()
                .onSuccessTask(new ProcessInternetAvailability())
                .onSuccess(new ShowLists(), UI_THREAD_EXECUTOR);
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
        dataRepo.getLocalList(spellingList.id()).continueWith(new ShowFirstWord(), UI_THREAD_EXECUTOR);
    }

    public void showHint() { view.showPopUp("The word is", spellingList.currentWord()); }

    public void updateText(String answerText) {
        view.setTheAnswer(answerText);
        view.setCompletionProgress(spellingList.primaryProgress(), spellingList.secondaryProgress());
    }

    public void keyPressed(char unicodeChar) {
        if (unicodeChar == CAPS_KEY) {
            view.shiftKeyboard(true);
            setAsCap = true;
            return;
        }
        if (unicodeChar == HINT_KEY) {
            showHint();
            return;
        }
        if (unicodeChar == CLEAR_KEY) { answerText = ""; }
        else {
            String temp = Character.toString(unicodeChar);
            if (setAsCap) {
                setAsCap = false;
                temp = temp.toUpperCase();
                view.shiftKeyboard(false);
            }
            answerText += temp;
            if (spellingList.isCorrectAnswer(answerText)) {
                answerText = "";
                if (spellingList.atEnd()) { view.popUpYouFinished(); }
                else {
                    spellingList.nextWord();
                    showHint();
                }
            }
        }
        updateText(answerText);
    }

    public void startOver() {
        spellingList.setAtStart();
        updateText(answerText);
        showHint();
    }

    public void setListTitle() { view.setListTitle(spellingList.id()); }

    class ShowLists implements Continuation<SpellingLists, Void> {
        @Override public Void then(Task<SpellingLists> task) throws Exception {
            spellingLists = task.getResult();
            if (spellingLists.empty()) { noData(); }
            else { showLists(); }
            return null;
        }
    }

    class ProcessInternetAvailability implements Continuation<Boolean, Task<SpellingLists>> {
        @Override public Task<SpellingLists> then(Task<Boolean> task) throws Exception {
            return task.getResult() ? dataRepo.syncData() : dataRepo.getLocalData();
        }
    }

    class ShowFirstWord implements Continuation<SpellingList, Void> {
        @Override public Void then(Task<SpellingList> task) throws Exception {
            spellingList = task.getResult();
            setListTitle();
            view.showPopUp("Let's get started with ", spellingList.currentWord());
            return null;
        }
    }
}
