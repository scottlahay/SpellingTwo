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
import scott.spelling.model.*;
import scott.spelling.presenter.*;
import scott.spelling.system.*;
import scott.spellingtwo.R;

import static android.R.anim.*;
import static android.R.id.*;
import static android.view.Gravity.*;
import static android.view.Window.*;
import static android.view.WindowManager.LayoutParams.*;
import static android.view.animation.AnimationUtils.*;
import static scott.spellingtwo.R.id.*;
import static scott.spellingtwo.R.layout.*;
import static scott.spellingtwo.R.style.*;

public class SpellingActivity extends Activity {

    @BindView(swtAnswer_p) TextSwitcher swtAnswer;
    @BindView(keyboardview) KeyboardView keyboardView;
    ProgressDialog progress;

    String answerText = "";
    TextToSpeech speak;
    SpellingLists lists;
    SpellingList list;
    String[] listNames;

    SpellingPresenter presenter;

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

    private void initUi() {
        initTheKeyboard();
        waitForTheProgramToLoad();
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

    private void updateHeader() {
        ((TextView) findViewById(txtCompletion)).setText(list.current + "/" + list.size());
    }

    public void popUpChooseList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, MyAlertDialogStyle);
        builder.setTitle("Pick your list");
        builder.setItems(listNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list = lists.findList(listNames[which]);
                dialog.dismiss();
                popUpStartingWord();
            }
        });
        builder.show();
    }

    private void buildDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, MyAlertDialogStyle);
        builder.setMessage(list.currentWord());
        builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        AppCompatDialog dialog = builder.show();
        TextView view = (TextView) dialog.findViewById(message);
        if (view != null) { view.setGravity(CENTER); }
    }

    public void popUpNoListsOrInternetConnection() {buildDialog("Sorry, the first time you use this app you need to have an internet connection. Please connect to the Internet"); }
    private void popUpCurrentWord() { buildDialog("The word is"); }
    private void popUpStartingWord() { buildDialog("Let's get started with ");}
    private void popUpYouFinished() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, MyAlertDialogStyle);
        builder.setTitle("Finished!");
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) { reset(); }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) { finish(); }
        });
        builder.show();
    }

    public void reset() {
        list.reset();
        hint(null);
        answerText = "";
        updateText();
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

    private void keyPressed(char unicodeChar) {
        String text = Character.toString(unicodeChar);
        answerText += text;
        updateText();
        if (list.isCorrectAnswer(answerText)) {
            if (list.finished()) {
                popUpYouFinished();
            }
            else {
                list.nextWord();
                answerText = "";
                updateText();
                hint(null);
            }
        }
    }

    private void updateText() {
        swtAnswer.setCurrentText(answerText);
        updateHeader();
    }

    public void hint(View v) {
        speak.speak(list.currentWord(), TextToSpeech.QUEUE_FLUSH, null, null);
        popUpCurrentWord();
    }

    public void clearTheWord(View v) {
        answerText = "";
        swtAnswer.setText(answerText);
    }

    @Override
    protected void onDestroy() {
        if (speak != null) {
            speak.stop();
            speak.shutdown();
        }
        super.onDestroy();
    }

    public void displayLists(SpellingLists spellingLists) {
        lists = spellingLists;
        listNames = lists.getAllListNames();
        textSwitchStuff();
        popUpChooseList();
    }
    public void showProgress() { progress.show(); }
    public void hideProgress() { progress.dismiss(); }
    public void exit(View view) {finish();}

    public class MyKeyPressListener extends ScottsKeyPressListener {
        @Override public void onPress(int primaryCode) {
            keyPressed((char) primaryCode);
        }
    }
}
