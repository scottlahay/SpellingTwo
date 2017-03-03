package scott.spelling.system;

import org.junit.*;

public class DataRepoTest {

    DataRepo repo;

    @Before
    public void before() {
       repo = new DataRepo();
    }

    @Test
    public void reposGetDataFromTheServer() throws Throwable {
//        repo.synchData(context);
        // test something

    }

    @Test
    public void reposDoNotGetDataIfTheyAlreadyHaveCurrentData() throws Throwable {
//        repo.synchData(context);


    }
}