package scott.spelling.presenter;

import org.greenrobot.eventbus.*;
import org.junit.*;

import bolts.*;
import scott.spelling.*;
import scott.spelling.model.*;
import scott.spelling.system.*;
import scott.spelling.view.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static scott.spelling.presenter.SpellingPresenter.*;
import static scott.spelling.system.TestUtils.*;
import static scott.spelling.utils.MockingUtils.*;

public class SpellingPresenterTest {

    SpellingPresenter presenter;

    @Before
    public void before() {
        InternetChecker checker = mock(InternetChecker.class);
        SpellingActivity view = mock(SpellingActivity.class);
        DataRepo dataRepo = mock(DataRepo.class);
        presenter = new SpellingPresenter(view, checker, dataRepo, mock(EventBus.class));
        presenter.spellingLists = spellingLists();
        presenter.spellingList = presenter.spellingLists.findList(0);
        Task mock = mock(Task.class);
        when(mock.onSuccess((Continuation) any())).thenReturn(mock);
        when(mock.onSuccessTask((Continuation) any())).thenReturn(mock);
        when(presenter.internet.availability()).thenReturn(mock);
        when(presenter.dataRepo.syncDataCheck()).thenReturn(mock);
        when(presenter.dataRepo.getLocalLists()).thenReturn(mock);
        when(presenter.dataRepo.getLocalList(anyString())).thenReturn(mock(Task.class));
        Task t = asTask(appData());
        when(presenter.dataRepo.getAppData()).thenReturn(t);
    }

    @Test
    public void userSeesAWaitScreenTillWeHaveSpellingListsToShowThem() throws Throwable {
        presenter.init();
        verify(presenter.view).showProgress();
    }

    @Test
    public void appsThatHaveNoInternetConnectionsCheckForLocalData() throws Throwable {
        presenter.new ProcessInternetAvailability().then(asTask(false));
        verify(presenter.dataRepo).getLocalLists();
    }

    @Test
    public void appsThatHaveAnInternetConnectionSyncWithTheDataOnTheServer() throws Throwable {
        presenter.new ProcessInternetAvailability().then(asTask(true));
        verify(presenter.dataRepo).syncDataCheck();
    }

    @Test
    public void appsThatHaveNoInternetConnectionButHavePreviouslyStoredDataLoadTheApp() throws Throwable {
        when(presenter.dataRepo.getLocalLists()).thenReturn(mock(Task.class));
        presenter.new ProcessInternetAvailability().then(asTask(false));
        verify(presenter.dataRepo).getLocalLists();
    }

    @Test
    public void whenTheDataFinishesSyncingThenWeShowTheUserTheSpellingLists() throws Throwable {
        presenter.showLists();
        verify(presenter.view).popUpChooseList((String[]) any());
        verify(presenter.view).hideProgress();
    }

    @Test
    public void whenTheAppHasNoDataAndCantGetDataWeDisplayAPopUpTellingTheUserToConnectToTheInternet() throws Throwable {
        presenter.new ShowLists().then(asTask(emptySpellingLists()));

        verify(presenter.view).hideProgress();
        verify(presenter.view).showPopUp(anyString(), anyString());
    }

    @Test
    public void whenTheAppHasDataWeLetTheUserChooseTheListTheyWantToUse() throws Throwable {
        presenter.new ShowLists().then(asTask(spellingLists()));
        verify(presenter.view).hideProgress();
        verify(presenter.view).popUpChooseList((String[]) any());
    }

    @Test
    public void whenTheUserChoosesThereListWePullItFromTheLocalCache() throws Throwable {

        presenter.startTestFor(spellingListWeekOne().id());
        assertEquals(spellingListWeekOne(), presenter.spellingList);
        verify(presenter.dataRepo).getLocalList(spellingListWeekOne().id());
    }

    @Test
    public void whenTheUserChoosesThereListWeDisplayTheFirstWord() throws Throwable {
        SpellingList obj = spellingListWeekOne();
        obj.add("four");
        presenter.new ShowFirstWord().then(asTask(obj));
        verify(presenter.view).showPopUp(anyString(), anyString());

        // we also assign the SpellingList from the database here to
        assertEquals(obj, presenter.spellingList);
    }

    @Test
    public void theUserCanAskToSeeTheWord() throws Throwable {
        presenter.showHint();
        verify(presenter.view).showPopUp(anyString(), anyString());
    }

    @Test
    public void whenTheUserEntersACharacterWeChangeTheDisplayToShowTheNewCharacter() throws Throwable {
        presenter.updateText("");
        verify(presenter.view).setTheAnswer(anyString());
        // we update the progress bar to, for when the user completes the word
        verify(presenter.view).setCompletionProgress(anyInt(), anyInt());
    }

    @Test
    public void theAnswerGetsUpdatedWhenTheUserPressesAKey() throws Throwable {
        presenter.keyPressed((char) 97);
        assertEquals("a", presenter.answerText);
        verify(presenter.view).setTheAnswer("a");
    }

    @Test
    public void capitalizeKeySetsTheNextLetterToComeThroughAsACapital() throws Throwable {
        presenter.keyPressed(CAPS_KEY);
        presenter.keyPressed((char) 97);
        assertEquals("A", presenter.answerText);

        // switches the keys to capital letters
        verify(presenter.view).shiftKeyboard(true);

        // but only the next character
        presenter.keyPressed((char) 97);
        assertEquals("Aa", presenter.answerText);
// switches the keyboard back to  lower case
        verify(presenter.view).shiftKeyboard(true);
    }

    @Test
    public void theClearKeyClearsTheAnswer() throws Throwable {
        presenter.answerText = "blah";
        presenter.keyPressed(CLEAR_KEY);
        assertEquals("", presenter.answerText);
        verify(presenter.view).setTheAnswer("");
    }

    @Test
    public void theHintKeyPopsUpTheHintText() throws Throwable {
        presenter.keyPressed(HINT_KEY);
        verify(presenter.view).showPopUp(anyString(), anyString());
    }

    @Test
    public void whenTheUserGetsTheCorrectAnswerWeMoveToTheNextWord() throws Throwable {
        presenter.answerText = "on";
        presenter.keyPressed((char) 101);
        assertEquals(presenter.spellingList.words().get(1), presenter.spellingList.currentWord());
        // reset the current answer
        assertEquals("", presenter.answerText);
        // update the answer
        verify(presenter.view).setTheAnswer("");
        // show the user the next word
        verify(presenter.view).showPopUp(anyString(), anyString());
    }

    @Test
    public void whenTheUserFinishesTheLastWordInAListWeGiveThemAChanceToStartOver() throws Throwable {
        presenter.spellingList.setAtEnd();
        String word = presenter.spellingList.currentWord();
        presenter.answerText = word.substring(0, word.length() - 1);
        presenter.keyPressed(word.charAt(word.length() - 1));

        verify(presenter.view).popUpYouFinished();
    }

    @Test
    public void usersThatChooseToStartOverStartTheListOver() throws Throwable {
        presenter.spellingList.setAtEnd();
        presenter.startOver();
        assertTrue(presenter.spellingList.atStart());
        // reset the view
        verify(presenter.view).setTheAnswer(anyString());
        // show the user the word
        verify(presenter.view).showPopUp(anyString(), anyString());
    }

    @Test
    public void theUserSeesTheNameOfTheSpellingListAtTheTopOfTheScreen() throws Throwable {
        presenter.setListTitle();
        verify(presenter.view).setListTitle(anyString());
    }

    @Test
    public void usersSeeTheLastListThatTheyHadOpenWhenTheyOpenTheApp() throws Throwable {
        presenter.init();
        verify(presenter.dataRepo).getAppData();
    }

    @Test
    public void whenUsersSelectANewList() throws Throwable {
        String testName = spellingListWeekTwo().id();
        presenter.event(new ListChangedEvent(testName));
        // update the View
        verify(presenter.view).showTest();
        // current list is updated
        assertEquals(testName, presenter.spellingList.id());
        // database is updated
        verify(presenter.dataRepo).updateAppData((AppData) any());


    }
}