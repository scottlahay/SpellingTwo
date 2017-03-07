package scott.spelling.utils;

import bolts.*;

import static org.mockito.Mockito.*;

public class MockingUtils {
    public static Task asTask(Object obj) {
        Task  mock = mock(Task.class);
        when(mock.getResult()).thenReturn(obj);
        return  mock;
    }
}
