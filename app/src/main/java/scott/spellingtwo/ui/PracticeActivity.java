package scott.spellingtwo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import scott.spellingtwo.R;
import scott.spellingtwo.domain.SpellingList;
import scott.spellingtwo.domain.SpellingLists;

public class PracticeActivity extends Activity {
    private String answerText = "";
    private TextSwitcher switcher;
    private TextToSpeech speak;
    private SpellingLists lists;
    private SpellingList list;
    private String[] listNames;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spelling_two);
        UpdateSpellingListsFromGoogleSheets updating = new UpdateSpellingListsFromGoogleSheets();
        updating.execute();
        waitForTheProgramToLoad();
        speak = new TextToSpeech(this, null);
        while (!updating.done) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                // not concerned if this blows chunkies
                System.out.println("thread bailed");
            }
        }
        progress.dismiss();
        haveTheUserChooseHisList();

    }

    public void waitForTheProgramToLoad() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

    private void haveTheUserChooseHisList() {
        listNames = lists.getAllListNames();
        textSwitchStuff();
        popUpChooseList();
    }

    private void updateHeader() {
        ((TextView) findViewById(R.id.completionLabel)).setText(list.current + "/" + list.size());
    }

    public void popUpChooseList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick your list");
        builder.setItems(listNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list = lists.findList(listNames[which]);
                popUpStartingWord();
            }
        });
        builder.show();
    }

    private void popUpCurrentWord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(list.currentWord());
        builder.setTitle("The word is");
        AlertDialog dialog = builder.show();
        TextView view = (TextView) dialog.findViewById(android.R.id.message);
        view.setGravity(Gravity.CENTER);
    }

    private void popUpStartingWord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(list.currentWord());
        builder.setTitle("Let's get started with ");
        AlertDialog dialog = builder.show();
        TextView view = (TextView) dialog.findViewById(android.R.id.message);
        view.setGravity(Gravity.CENTER);
    }

    private void popUpYouFinished() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finished!");
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exit(null);
            }
        });
        builder.show();
    }

    public void exit(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void reset() {
        list.reset();
        hint(null);
        answerText = "";
        updateText();
    }

    public void textSwitchStuff() {
        switcher = (TextSwitcher) findViewById(R.id.answerSwitcher);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(PracticeActivity.this);
                myText.setGravity(Gravity.CENTER);
                myText.setTextSize(100);
                myText.setTextColor(Color.BLACK);
                myText.setTextIsSelectable(false);
                return myText;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        switcher.setInAnimation(in);
        switcher.setOutAnimation(out);
    }

    public void userButtonPress(View v) {
        answerText += ((Button) v).getText().toString();
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
    }

    private void updateText() {
        switcher.setCurrentText(answerText);
        updateHeader();
    }

    public void hint(View v) {
        speak.speak(list.currentWord(), TextToSpeech.QUEUE_FLUSH, null, null);
        popUpCurrentWord();
    }

    public void clearTheWord(View v) {
        answerText = "";
        switcher.setText(answerText);
    }

    @Override
    protected void onDestroy() {
        if (speak != null) {
            speak.stop();
            speak.shutdown();
        }
        super.onDestroy();
    }

    public class UpdateSpellingListsFromGoogleSheets extends AsyncTask<String, Void, Void> {
        boolean done;

        @Override
        protected Void doInBackground(String... params) {
            lists = SpellingLists.load();
            done = true;
            return null;
        }
    }
}
