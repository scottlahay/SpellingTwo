package scott.spelling.presenter;

import android.content.*;

import org.junit.*;

import bolts.*;
import scott.spelling.system.*;
import scott.spelling.view.*;

import static org.mockito.Mockito.*;
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
        when(presenter.dataRepo.synchData((Context) any())).thenReturn(mock(Task.class));

    }

    @Test
    public void userSeesAWaitScreenTillWeHaveSpellingListsToShowThem() throws Throwable {
        presenter.init();
        verify(presenter.view).showProgress();
        // this is also a good time to check the internet availability
        verify(presenter.internet).availability();
    }

    @Test
    public void appsThatHaveNoDataOrInternetConnectionsShowAHelpPage() throws Throwable {
        when(presenter.dataRepo.hasLocalData()).thenReturn(false);
        presenter.new ProcessInternetAvailability().then(asTask(false));
        verify(presenter.view).displayHelpPage();

        // also make sure that we hide the progress here to, since we have to cut the loading short
        verify(presenter.view).hideProgress();
    }

    @Test
    public void appsThatHaveNoInternetConnectionButHavePreviouslyStoredDataLoadTheApp() throws Throwable {
        when(presenter.dataRepo.getLocalData((Context) any())).thenReturn(mock(Task.class));
        when(presenter.dataRepo.hasLocalData()).thenReturn(true);
        presenter.new ProcessInternetAvailability().then(asTask(false));
        verify(presenter.dataRepo).getLocalData((Context) any());
    }

    @Test
    public void appsThatHaveAnInternetConnectionSyncWithTheDataOnTheServer() throws Throwable {
        presenter.new ProcessInternetAvailability().then(asTask(true));
        verify(presenter.dataRepo).synchData((Context) any());
    }
//
//    @Test
//    public void whenTheDataFinishesSyncingThenWeShowTheUserTheSpellingLists() throws Throwable {
//        presenter.showLists();
//        verify(presenter.view).displayLists((SpellingLists) any());
//        verify(presenter.view).hideProgress();
//    }
}