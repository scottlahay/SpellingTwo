package scott.spelling.view;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.inputmethodservice.*;
import android.os.*;
import android.speech.tts.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

import butterknife.*;
import scott.spelling.presenter.*;
import scott.spelling.system.*;
import scott.spellingtwo.R;

import static android.R.anim.*;
import static android.speech.tts.TextToSpeech.*;
import static android.view.Gravity.*;
import static android.view.Window.*;
import static android.view.WindowManager.LayoutParams.*;
import static android.view.animation.AnimationUtils.*;
import static scott.spellingtwo.R.id.*;
import static scott.spellingtwo.R.layout.*;
import static scott.spellingtwo.R.style.*;

public class SpellingActivity extends Activity {

    public SpellingPresenter presenter;
    @BindView(swtAnswer_p) TextSwitcher swtAnswer;
    @BindView(keyboardview) KeyboardView keyboardView;
    ProgressDialog progress;
    TextToSpeech speak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(activity_spelling_two);
        ButterKnife.bind(this);
        speak = new TextToSpeech(this, null);
        initUi();
        presenter = new SpellingPresenter(this, new InternetChecker(this), new DataRepo(this));
        presenter.init();
    }

    void initUi() {
        initTheKeyboard();
        waitForTheProgramToLoad();
        textSwitchStuff();
    }

    public void initTheKeyboard() {
        int my_keyboard = R.xml.my_keyboard;
        Keyboard keyboard = new Keyboard(this, my_keyboard);
        keyboardView = (KeyboardView) findViewById(R.id.keyboardview);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(new MyKeyPressListener());
    }

    public void waitForTheProgramToLoad() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait while loading...");
        progress.setCancelable(false);
    }

    public void updateHeader(String text) { ((TextView) findViewById(txtCompletion)).setText(text); }

    public void popUpChooseList(String[] lists) {
        new AlertDialog.Builder(this, MyAlertDialogStyle)
                .setTitle("Pick your list")
                .setItems(lists, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int listIndex) {
                        dialog.dismiss();
                        presenter.userSelectsThereList(listIndex);
                    }
                })
                .show();
    }

    public void showPopUp(String title, String message) {
        AppCompatDialog dialog = new AlertDialog.Builder(this, MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null).show();

        TextView view = (TextView) dialog.findViewById(android.R.id.message);
        if (view != null) { view.setGravity(CENTER); }
    }

    public void popUpYouFinished() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, MyAlertDialogStyle);
        builder.setTitle("Finished!");
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) { presenter.startOver(); }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) { finish(); }
        });
        builder.show();
    }

    public void textSwitchStuff() {
        swtAnswer = (TextSwitcher) findViewById(swtAnswer_p);
        swtAnswer.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(SpellingActivity.this);
                myText.setGravity(CENTER);
                myText.setTextSize(50);
                myText.setTextColor(Color.BLACK);
                myText.setTextIsSelectable(false);
                return myText;
            }
        });
        swtAnswer.setInAnimation(loadAnimation(this, slide_in_left));
        swtAnswer.setOutAnimation(loadAnimation(this, slide_out_right));
    }


    @Override
    protected void onDestroy() {
        if (speak != null) {
            speak.stop();
            speak.shutdown();
        }
        super.onDestroy();
    }

    void keyPressed(char unicodeChar) { presenter.keyPressed(unicodeChar); }
    public void setTheAnswer(String answerText) {swtAnswer.setCurrentText(answerText);}
    public void hint(View v) { presenter.showHint(); }
    public int speak(String word) {return speak.speak(word, QUEUE_FLUSH, null, null);}
    public void showProgress() { progress.show(); }
    public void hideProgress() { progress.dismiss(); }

    public class MyKeyPressListener extends ScottsKeyPressListener {
        @Override public void onPress(int primaryCode) {
            keyPressed((char) primaryCode);
        }
    }
}
