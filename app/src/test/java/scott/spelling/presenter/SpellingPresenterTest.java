package scott.spelling.presenter;

import org.junit.*;

import bolts.*;
import scott.spelling.model.*;
import scott.spelling.system.*;
import scott.spelling.view.*;

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
        when(presenter.internet.availability()).thenReturn(mock(Task.class));
        when(presenter.dataRepo.synchData()).thenReturn(mock(Task.class));
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
        verify(presenter.dataRepo).synchData();
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
        verify(presenter.view).displayLists((SpellingLists) any());
        verify(presenter.view).hideProgress();
    }

    @Test
    public void whenTheAppHasNoDataAndCantGetDataWeDisplayAPopUpTellingTheUserToConnectToTheInternet() throws Throwable {
        presenter.new ShowLists().then(asTask(emptySpellingLists()));

        verify(presenter.view).hideProgress();
        verify(presenter.view).popUpNoListsOrInternetConnection();
    }

    @Test
    public void whenTheAppHasDataWeLetTheUserChooseTheListTheyWantToUse() throws Throwable {
        presenter.new ShowLists().then(asTask(spellingLists()));
        verify(presenter.view).hideProgress();
        verify(presenter.view).displayLists((SpellingLists) any());
    }
}