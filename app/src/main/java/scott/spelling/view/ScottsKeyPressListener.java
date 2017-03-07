package scott.spelling.view;

import android.inputmethodservice.*;

// stub class to hide unimplemented methods
public abstract class ScottsKeyPressListener implements KeyboardView.OnKeyboardActionListener {

    @Override public void onRelease(int primaryCode) { }
    @Override public void onKey(int primaryCode, int[] keyCodes) { }
    @Override public void onText(CharSequence text) { }
    @Override public void swipeLeft() { }
    @Override public void swipeRight() { }
    @Override public void swipeDown() { }
    @Override public void swipeUp() { }
}
