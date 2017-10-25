package scott.spelling.system;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataRepoTest {

    DataRepo repo;

    @Before
    public void before() {
        repo = new DataRepo(mock(LocalCache.class));
    }

    @Test
    public void asSoonAsWeHaveAnInternetConnectionWeCheckToSeeIfWeNeedToPullInTheLatestSpellingLists() throws Throwable {
        fail();
//        repo.syncDataCheck();
//        verify(repo.localCache).getAppData();
//        verify(repo.syncDataCheck());
//                if key is empty
//                        get the lists from the server
//                        save them to the local cache
//                        save app data
//                - tell the lists to load the local data
    }

    @Test
    public void weCanCheckForStoredData() throws Throwable {
        fail();
    }

    @Test
    public void reposGetDataFromTheServer() throws Throwable {
        fail();
    }

    @Test
    public void reposDoNotGetDataIfTheyAlreadyHaveCurrentData() throws Throwable {
        fail();
    }
}