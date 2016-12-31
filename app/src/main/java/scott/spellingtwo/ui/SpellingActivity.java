package scott.spellingtwo.ui;

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

import com.scott.spellingdomain.*;

import butterknife.*;
import scott.spellingtwo.domain.*;

import static android.R.anim.*;
import static android.R.id.*;
import static android.view.Gravity.*;
import static android.view.Window.*;
import static android.view.WindowManager.LayoutParams.*;
import static android.view.animation.AnimationUtils.*;
import static com.scott.spellingdomain.UrlUtil.*;
import static scott.spellingtwo.R.id.*;
import static scott.spellingtwo.R.layout.*;
import static scott.spellingtwo.R.style.*;

public class SpellingActivity extends Activity {

  // todo finish off the caching changes
     // todo keyboard change return to clear, remove characters that I don't want kids to use.
    // todo change the list order to numerical;

    @BindView(swtAnswer_p) TextSwitcher swtAnswer;
    ProgressDialog progress;

    String answerText = "";
    TextToSpeech speak;
    SpellingLists lists;
    SpellingList list;
    String[] listNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(activity_spelling_two);
        ButterKnife.bind(this);
        initTheKeyboard();
        UpdateSpellingListsFromGoogleSheets updating = new UpdateSpellingListsFromGoogleSheets();
        updating.execute();
        waitForTheProgramToLoad();
        speak = new TextToSpeech(this, null);

        // todo move this to a callback
        while (!updating.done) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
        progress.dismiss();
        haveTheUserChooseHisList();
    }

    public void initTheKeyboard()
    {
        Keyboard mKeyboard= new Keyboard(this, scott.spellingtwo.R.xml.my_keyboard);
        KeyboardView mKeyboardView= (KeyboardView)findViewById(scott.spellingtwo.R.id.keyboardview);
        mKeyboardView.setKeyboard( mKeyboard );
        mKeyboardView.setPreviewEnabled(false);
    }


    public void waitForTheProgramToLoad() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

    private void haveTheUserChooseHisList() {
        listNames = lists.getAllListNames();
        textSwitchStuff();
        popUpChooseList();
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
        view.setGravity(CENTER);
    }

    private void popUpCurrentWord() { buildDialog("The word is"); }
    private void popUpStartingWord() { buildDialog("Let's get started with ");}
    private void popUpYouFinished() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,MyAlertDialogStyle);
        builder.setTitle("Finished!");
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {return true;}
        String text = Character.toString((char)event.getUnicodeChar());
        answerText += text;
        updateText();
        if (list.isCorrectAnswer(answerText)) {
            if (list.finished()) {
                popUpYouFinished();
            } else {
                list.nextWord();
                answerText = "";
                updateText();
                hint(null);
            }
        }
        return super.dispatchKeyEvent(event);
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
    public void exit(View view) {finish();}

    public class UpdateSpellingListsFromGoogleSheets extends AsyncTask<String, Void, Void> {
        boolean done;

        @Override
        protected Void doInBackground(String... params) {
            lists = new SpellingLists(new FileDownloader().download(KIDS_SPELLING));
            done = true;
            return null;
        }
    }
}
