package scott.spelling.presenter

import android.content.DialogInterface
import bolts.Continuation
import bolts.Task
import org.greenrobot.eventbus.EventBus
import scott.spelling.model.SpellingList
import scott.spelling.model.SpellingLists
import scott.spelling.model.Utils.asList
import scott.spelling.system.InternetChecker
import scott.spelling.view.MainActivity
import java.util.*

class SpellingPresenter(internal var view: MainActivity, internal var internet: InternetChecker, eventBus: EventBus) {
    internal var spellingLists: SpellingLists? = null
    internal var spellingList: SpellingList? = null
    internal var answerText = ""
    internal var setAsCap: Boolean = false

    fun init() {

    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void event(ListChangedEvent event) {
    //        appData.setCurrentTest(event.listName);
    //        dataRepo.updateAppData(appData);
    //        Task.callInBackground(new StartATask()).onSuccessTask(new LoadListData()).onSuccess(new StartSpellingTest(), UI_THREAD_EXECUTOR);
    //    }

    private fun noData() {
        view.hideProgress()
        val message = "Sorry, the first time you use this app you need to have an internet connection. Please connect to the Internet"
        view.showPopUp(message, "", view.closeApp)
    }

    fun startSpellingTest() {
        setListTitle()
        view.showTest()
        view.hideProgress()
        updateText("")
        showHint(HintClosed())
    }

    fun showHint(hintAction: HintClosed) {
        view.showPopUp("Spell", spellingList!!.currentWord(), hintAction)
    }

    fun updateText(answerText: String) {
        this.answerText = answerText
        view.setTheAnswer(answerText)
        view.setCompletionProgress(spellingList!!.primaryProgress())
    }

    fun keyPressed(unicodeChar: Char) {
        if (unicodeChar == CAPS_KEY) {
            view.shiftKeyboard(true)
            setAsCap = true
            return
        }
        if (unicodeChar == HINT_KEY) {
//            showHint(null)
            return
        }
        if (unicodeChar == CLEAR_KEY) {
            updateText("")
        }
        else {
            var temp = Character.toString(unicodeChar)
            if (setAsCap) {
                setAsCap = false
                temp = temp.toUpperCase()
                view.shiftKeyboard(false)
            }
            updateText("$answerText$temp")
            if (spellingList!!.isCorrectAnswer(answerText)) {
                view.stopCapturingTyping()
                view.showCheck()
                if (spellingList!!.atEnd()) {
                    view.popUpYouFinished()
                }
                else {
                    spellingList!!.nextWord()
                    showHint(HintClosed())
                }
            }
        }
    }

    fun startOver() {
        spellingList!!.setAtStart()
        updateText(answerText)
        showHint(HintClosed())
    }

    fun setListTitle() {
        view.setListTitle(spellingList!!.id)
    }

    fun changeSpellingList() {
        val map = HashMap<String, List<String>>()
        map.put("Grade 6", spellingLists!!.listNames())
        view.showListChooser(asList("Grade 6"), map)
    }

    internal inner class StartSpellingTest : Continuation<Void, Void> {
        @Throws(Exception::class)
        override fun then(task: Task<Void>): Void? {
            startSpellingTest()
            return null
        }
    }

    inner class HintClosed : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            view.captureTyping()
            view.hideCheck()
            answerText = ""
            updateText(answerText)
        }
    }

    companion object {
        val CAPS_KEY = (-1).toChar()
        val CLEAR_KEY = (-4).toChar()
        val HINT_KEY = (-5).toChar()
    }
}
