package com.scott.spellingdomain;

import org.junit.*;
import static org.mockito.Mockito.*;

public class UserOpensTheAppTest {

    ShouldUpdateSpellingLists updater;
    LocalCache localCache;
    InternetChecker internetChecker;
    SpellingPresenter presenter;
    SpellingList spellingList;

    @Before
    public void before() throws Exception {
        updater = new ShouldUpdateSpellingLists();
        localCache = mock(LocalCache.class);
        internetChecker = mock(InternetChecker.class);
        presenter = mock(SpellingPresenter.class);
        spellingList = mock(SpellingList.class);
        when(internetChecker.hasInternet()).thenReturn(true);
        when(internetChecker.getLists()).thenReturn(spellingList);
        when(internetChecker.listIsOld(localCache)).thenReturn(false);
        when(localCache.isEmpty()).thenReturn(false);
    }

    @Test
    public void spellingListAreDisplayedToTheUser() throws Throwable {
        updater.updateLists(presenter, internetChecker, localCache);
        verify(localCache, never()).setList((SpellingList) any());
        verify(presenter).displayListChooser();
    }

    @Test
    public void whenTheSpellingListsIsEmptyWeSendTheUserToASplashPage() throws Throwable {
        when(localCache.isEmpty()).thenReturn(true);
        when(internetChecker.hasInternet()).thenReturn(false);
        updater.updateLists(presenter, internetChecker, localCache);
        verify(presenter).showEmptyListSplashPage();
    }

    @Test
    public void appsWithAnEmptyCacheLoadTheCacheFromGoogleSheets() throws Throwable {
        when(localCache.isEmpty()).thenReturn(true);
        updater.updateLists(presenter, internetChecker, localCache);
        verify(localCache).setList(spellingList);
    }

    @Test
    public void appsThatDoNotHaveTheLatestListLoadTheCacheFromGoogleSheets() throws Throwable {
        when(internetChecker.listIsOld(localCache)).thenReturn(true);
        updater.updateLists(presenter, internetChecker, localCache);
        verify(localCache).setList(spellingList);
        verify(presenter).displayListChooser();
    }
}