package scott.spelling.view;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.inputmethodservice.*;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.akexorcist.roundcornerprogressbar.*;
import com.mikepenz.materialdrawer.*;

import butterknife.*;
import scott.spelling.R;
import scott.spelling.presenter.*;
import scott.spelling.system.*;

import static android.R.anim.*;
import static android.view.Gravity.*;
import static android.view.Window.*;
import static android.view.WindowManager.LayoutParams.*;
import static android.view.animation.AnimationUtils.*;
import static scott.spelling.R.id.*;
import static scott.spelling.R.layout.*;
import static scott.spelling.R.style.*;

public class SpellingActivity extends AppCompatActivity {

    //@todo love the cloud background and gears here https://github.com/lj-3d/GearLoadingProject

    public SpellingPresenter presenter;
    public Keyboard keyboard;
    @BindView(txt_title) TextView txtTitle;
    @BindView(swt_Answer_p) TextSwitcher swtAnswer;
    @BindView(keyboard_view_p) KeyboardView keyboardView;
    @BindView(prg_list_progress_p) RoundCornerProgressBar prgListProgress;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(activity_spelling);
        try {
            throw new NullPointerException();
//            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tempInitUi();
        initUi();
        presenter = new SpellingPresenter(this, new InternetChecker(this), new DataRepo(this));
        presenter.init();
    }
    private void tempInitUi() { // @TODO this is temporary till I can figure out why ButterKnife Is having issues, Probably something to do with the proguard setup,
        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view_p);
        prgListProgress = (RoundCornerProgressBar) findViewById(prg_list_progress_p);
        swtAnswer = (TextSwitcher) findViewById(swt_Answer_p);
    }

    void initUi() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.my_action_bar);
        new DrawerBuilder().withActivity(this).build();
        txtTitle = (TextView) findViewById(R.id.txt_title);
        initTheKeyboard();
        waitForTheProgramToLoad();
        textSwitchStuff();

    }

    public void initTheKeyboard() {

        int my_keyboard = R.xml.my_keyboard;
        keyboard = new Keyboard(this, my_keyboard);

        keyboardView.setKeyboard(keyboard);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(new MyKeyPressListener());
    }

    public void shiftKeyboard(boolean shifted) {
        keyboard.setShifted(shifted);
        keyboardView.invalidateAllKeys();
    }

    public void waitForTheProgramToLoad() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait while loading...");
        progress.setCancelable(false);
    }

    public void setCompletionProgress(int currentProgress, int secondaryProgress) {
        prgListProgress.setProgress(currentProgress);
//        prgListProgress.setSecondaryProgress(secondaryProgress);
    }

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

        if (view != null) {
            view.setTextSize(50);
            view.setGravity(CENTER);
        }
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
        swtAnswer = (TextSwitcher) findViewById(swt_Answer_p);
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

    public void keyPressed(char unicodeChar) { presenter.keyPressed(unicodeChar); }
    public void setTheAnswer(String answerText) {swtAnswer.setCurrentText(answerText);}
    public void showProgress() { progress.show(); }
    public void hideProgress() { progress.dismiss(); }
    public void setListTitle(String title) { txtTitle.setText(title);}
    public class MyKeyPressListener extends ScottsKeyPressListener {
        @Override public void onPress(int primaryCode) {
            Log.d("dddddddd", "onPress: ");
            keyPressed((char) primaryCode);
        }
    }
}
