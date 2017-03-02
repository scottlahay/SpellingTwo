package scott.spelling.presenter;

import org.junit.*;

import scott.spelling.system.*;

import static org.mockito.Mockito.*;

public class SpellingPresenterTest {

    SpellingPresenter presenter;

    @Before
    public void before() {
        InternetChecker checker = mock(InternetChecker.class);
        SpellingPresenter.SpellingView view = mock(SpellingPresenter.SpellingView.class);
        DataRepo dataRepo = mock(DataRepo.class);
        presenter = new SpellingPresenter(view, checker, dataRepo);
        when(presenter.internet.available()).thenReturn(true);
    }

    @Test
    public void appsThatHaveNoDataOrInternetConnectionsShowAHelpPage() throws Throwable {
        when(presenter.internet.available()).thenReturn(false);
        when(presenter.dataRepo.hasLocalData()).thenReturn(false);

        presenter.init();
        verify(presenter.view).displayHelpPage();
    }


    @Test
    public void appsThatHaveNoInternetConnectionButHavePreviouslyStoredDataLoadTheApp() throws Throwable {
        when(presenter.internet.available()).thenReturn(false);
        when(presenter.dataRepo.hasLocalData()).thenReturn(true);
        presenter.init();
        verify(presenter.view).displayLists();
    }

    @Test
    public void appsThatHaveAnInternetConnectionSynchWithTheDataOnTheServer() throws Throwable {
        presenter.init();
        verify(presenter.view).showProgress();
        verify(presenter.dataRepo).synchData();
    }

    @Test
    public void whenTheDataFinishesSyncingThenWeShowTheUserTheSpellingLists() throws Throwable {
        presenter.showLists();
        verify(presenter.view).displayLists();
        verify(presenter.view).hideProgress();
    }


}