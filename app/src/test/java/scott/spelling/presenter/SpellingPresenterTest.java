package scott.spelling.presenter;

import org.junit.*;

import bolts.*;
import scott.spelling.system.*;
import scott.spelling.view.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static scott.spelling.system.TestUtils.*;
import static scott.spelling.utils.MockingUtils.*;

public class SpellingPresenterTest {

    SpellingPresenter presenter;

    @Before
    public void before() {
        InternetChecker checker = mock(InternetChecker.class);
        SpellingActivity view = mock(SpellingActivity.class);
        DataRepo dataRepo = mock(DataRepo.class);
        presenter = new SpellingPresenter(view, checker, dataRepo);
        presenter.spellingLists = spellingLists();
        presenter.spellingList = presenter.spellingLists.findList(0);
        when(presenter.internet.availability()).thenReturn(mock(Task.class));
        when(presenter.dataRepo.syncData()).thenReturn(mock(Task.class));
        when(presenter.dataRepo.getLocalData()).thenReturn(mock(Task.class));
    }

    @Test
    public void userSeesAWaitScreenTillWeHaveSpellingListsToShowThem() throws Throwable {
        presenter.init();
        verify(presenter.view).showProgress();
        // this is also a good time to check the internet availability
        verify(presenter.internet).availability();
    }

    @Test
    public void appsThatHaveNoInternetConnectionsCheckForLocalData() throws Throwable {
        presenter.new ProcessInternetAvailability().then(asTask(false));
        verify(presenter.dataRepo).getLocalData();
    }

    @Test
    public void appsThatHaveAnInternetConnectionSyncWithTheDataOnTheServer() throws Throwable {
        presenter.new ProcessInternetAvailability().then(asTask(true));
        verify(presenter.dataRepo).syncData();
    }

    @Test
    public void appsThatHaveNoInternetConnectionButHavePreviouslyStoredDataLoadTheApp() throws Throwable {
        when(presenter.dataRepo.getLocalData()).thenReturn(mock(Task.class));
        presenter.new ProcessInternetAvailability().then(asTask(false));
        verify(presenter.dataRepo).getLocalData();
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
        when(presenter.dataRepo.getLocalList(anyString())).thenReturn(mock(Task.class));

        presenter.userSelectsThereList(0);
        assertEquals(spellingList(), presenter.spellingList);
        verify(presenter.dataRepo).getLocalList(spellingList().id());
    }

    @Test
    public void whenTheUserChoosesThereListWeDisplayTheFirstWord() throws Throwable {
        presenter.new ShowFirstWord().then(asTask(spellingList()));
        verify(presenter.view).showPopUp(anyString(), anyString());
    }

    @Test @Ignore //Todo if necessary
    public void theUserSeesAHeaderAtTheTopShowingHowFarIntoTheTestTheyAre() throws Throwable {
        presenter.updateHeader(null);
    }

    @Test
    public void whenTheUserIsNotSureWhatTheWordIsWeGiveThemAHintBySpeakingAndSayingTheWord() throws Throwable {
        presenter.showHint();
        verify(presenter.view).speak(anyString());
        verify(presenter.view).showPopUp(anyString(), anyString());
    }

    @Test
    public void whenTheUserEntersACharacterWeChangeTheDisplayToShowTheNewCharacter() throws Throwable {
        presenter.updateText("");
        verify(presenter.view).setTheAnswer(anyString());
        // we update the header to, for when the user completes the word
        verify(presenter.view).updateHeader(anyString());
    }

    @Test
    public void theAnswerGetsUpdatedWhenTheUserPressesAKey() throws Throwable {
        presenter.keyPressed((char) 77);
        assertEquals("M", presenter.answerText);
        verify(presenter.view).setTheAnswer("M");
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
}