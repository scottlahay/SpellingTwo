package scott.spelling.presenter;

import org.greenrobot.eventbus.*;
import org.junit.*;

import bolts.*;
import scott.spelling.*;
import scott.spelling.system.*;
import scott.spelling.view.*;

import static org.mockito.Mockito.*;
import static scott.spelling.presenter.SpellingPresenter.*;
import static scott.spelling.system.TestUtils.*;

public class SpellingPresenterTest {

    SpellingPresenter presenter;

    @Before
    public void before() {
        InternetChecker checker = mock(InternetChecker.class);
        MainActivity view = mock(MainActivity.class);
        DataRepo dataRepo = mock(DataRepo.class);
        presenter = new SpellingPresenter(view, checker, mock(EventBus.class));
        presenter.setSpellingLists(spellingLists());
        presenter.setSpellingList(presenter.getSpellingLists().findList(0));
        Task mock = mock(Task.class);
        when(mock.onSuccess((Continuation) any())).thenReturn(mock);
        when(mock.onSuccessTask((Continuation) any())).thenReturn(mock);
        when(presenter.getInternet().availability()).thenReturn(mock);
//        when(presenter.dataRepo.syncDataCheck()).thenReturn(mock);
//        when(presenter.dataRepo.getLocalLists()).thenReturn(mock);
//        when(presenter.dataRepo.getLocalList(anyString())).thenReturn(mock(Task.class));
//        Task t = asTask(appData());
//        when(presenter.dataRepo.initDbIfNecessary()).thenReturn(t);
    }

    @Test
    public void userSeesAWaitScreenTillWeHaveSpellingListsToShowThem() throws Throwable {
        presenter.init();
        verify(presenter.getView()).showProgress();
    }

//    @Test
//    public void appsThatHaveNoInternetConnectionsCheckForLocalData() throws Throwable {
//        presenter.new ProcessInternetAvailability().then(asTask(false));
//        verify(presenter.dataRepo).getLocalLists();
//    }
//
//    @Test
//    public void appsThatHaveAnInternetConnectionSyncWithTheDataOnTheServer() throws Throwable {
//        presenter.new ProcessInternetAvailability().then(asTask(true));
//        verify(presenter.dataRepo).syncDataCheck();
//    }

//    @Test
//    public void appsThatHaveNoInternetConnectionButHavePreviouslyStoredDataLoadTheApp() throws Throwable {
//        when(presenter.dataRepo.getLocalLists()).thenReturn(mock(Task.class));
//        presenter.new ProcessInternetAvailability().then(asTask(false));
//        verify(presenter.dataRepo).getLocalLists();
//    }
//
//    @Test
//    public void whenTheDataFinishesSyncingThenWeShowTheUserTheSpellingLists() throws Throwable {
//        presenter.showLists();
//        verify(presenter.view).popUpChooseList((String[]) any());
//        verify(presenter.view).hideProgress();
//    }
//
//    @Test
//    public void whenTheAppHasNoDataAndCantGetDataWeDisplayAPopUpTellingTheUserToConnectToTheInternet() throws Throwable {
//        presenter.new ShowLists().then(asTask(emptySpellingLists()));
//
//        verify(presenter.view).hideProgress();
//        verify(presenter.view).showPopUp(anyString(), anyString(), listener);
//    }
//
//    @Test
//    public void whenTheAppHasDataWeLetTheUserChooseTheListTheyWantToUse() throws Throwable {
//        presenter.new ShowLists().then(asTask(spellingLists()));
//        verify(presenter.view).hideProgress();
//        verify(presenter.view).popUpChooseList((String[]) any());
//    }

//    @Test
//    public void whenTheUserChoosesThereListWePullItFromTheLocalCache() throws Throwable {
//
//        presenter.startSpellingTest(spellingListWeekOne().id());
//        assertEquals(spellingListWeekOne(), presenter.spellingList);
//        verify(presenter.dataRepo).getLocalList(spellingListWeekOne().id());
//    }

//    @Test
//    public void whenTheUserChoosesThereListWeDisplayTheFirstWord() throws Throwable {
//        Week obj = spellingListWeekOne();
//        obj.add("four");
//        presenter.new StartSpellingTest().then(asTask(obj));
//        verify(presenter.view).showPopUp(anyString(), anyString(), listener);
//
//        // we also assign the Week from the database here to
//        assertEquals(obj, presenter.spellingList);
//    }

//    @Test
//    public void theUserCanAskToSeeTheWord() throws Throwable {
//        presenter.showHint(new SpellingPresenter.HintClosed());
//        verify(presenter.view).showPopUp(anyString(), anyString(), listener);
//    }

    @Test
    public void whenTheUserEntersACharacterWeChangeTheDisplayToShowTheNewCharacter() throws Throwable {
        presenter.updateText("");
        verify(presenter.getView()).setTheAnswer(anyString());
        // we update the progress bar to, for when the user completes the word
        verify(presenter.getView()).setCompletionProgress(anyInt());
    }

    @Test
    public void theAnswerGetsUpdatedWhenTheUserPressesAKey() throws Throwable {
        presenter.keyPressed((char) 97);
        assertEquals("a", presenter.getAnswerText());
        verify(presenter.getView()).setTheAnswer("a");
    }

    @Test
    public void capitalizeKeySetsTheNextLetterToComeThroughAsACapital() throws Throwable {
        presenter.keyPressed(Companion.getCAPS_KEY());
        presenter.keyPressed((char) 97);
        assertEquals("A", presenter.getAnswerText());

        // switches the keys to capital letters
        verify(presenter.getView()).shiftKeyboard(true);

        // but only the next character
        presenter.keyPressed((char) 97);
        assertEquals("Aa", presenter.getAnswerText());
// switches the keyboard back to  lower case
        verify(presenter.getView()).shiftKeyboard(true);
    }

    @Test
    public void theClearKeyClearsTheAnswer() throws Throwable {
        presenter.setAnswerText("blah");
        presenter.keyPressed(Companion.getCLEAR_KEY());
        assertEquals("", presenter.getAnswerText());
        verify(presenter.getView()).setTheAnswer("");
    }

//    @Test
//    public void theHintKeyPopsUpTheHintText() throws Throwable {
//        presenter.keyPressed(HINT_KEY);
//        verify(presenter.view).showPopUp(anyString(), anyString(), listener);
//    }

//    @Test
//    public void whenTheUserGetsTheCorrectAnswerWeMoveToTheNextWord() throws Throwable {
//        presenter.answerText = "on";
//        presenter.keyPressed((char) 101);
//        assertEquals(presenter.spellingList.words().get(1), presenter.spellingList.currentWord());
//        // reset the current answer
//        assertEquals("", presenter.answerText);
//        // update the answer
//        verify(presenter.view).setTheAnswer("");
//        // show the user the next word
//        verify(presenter.view).showPopUp(anyString(), anyString(), listener);
//    }

    @Test
    public void whenTheUserFinishesTheLastWordInAListWeGiveThemAChanceToStartOver() throws Throwable {
        presenter.getSpellingList().setAtEnd();
        String word = presenter.getSpellingList().currentWord();
        presenter.setAnswerText(word.substring(0, word.length() - 1));
        presenter.keyPressed(word.charAt(word.length() - 1));

        verify(presenter.getView()).popUpYouFinished();
    }

    @Test
    public void theUserSeesTheNameOfTheSpellingListAtTheTopOfTheScreen() throws Throwable {
        presenter.setListTitle();
        verify(presenter.getView()).setListTitle(anyString());
    }

//    @Test
//    public void usersThatChooseToStartOverStartTheListOver() throws Throwable {
//        presenter.spellingList.setAtEnd();
//        presenter.startOver();
//        assertTrue(presenter.spellingList.atStart());
//        // reset the view
//        verify(presenter.view).setTheAnswer(anyString());
//        // show the user the word
//        verify(presenter.view).showPopUp(anyString(), anyString(), listener);
//    }

    @Test
    public void usersSeeTheLastListThatTheyHadOpenWhenTheyOpenTheApp() throws Throwable {
        presenter.init();
        verify(presenter.dataRepo).initDbIfNecessary();
    }

    @Test
    public void whenUsersSelectANewList() throws Throwable {
        String testName = spellingListWeekTwo().getName();
        presenter.event(new ListChangedEvent(testName));
        // update the View
        verify(presenter.getView()).showTest();
        // current list is updated
        assertEquals(testName, presenter.getSpellingList().getId());
        // database is updated
        verify(presenter.dataRepo).updateAppData((AppData) any());


    }
}