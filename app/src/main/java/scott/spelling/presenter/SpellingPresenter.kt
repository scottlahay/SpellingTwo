package scott.spelling.presenter

import android.content.DialogInterface
import bolts.Continuation
import bolts.Task
import scott.spelling.model.Grades
import scott.spelling.view.MainActivity

class SpellingPresenter(var view: MainActivity) {
    var grades: Grades? = null
    var answerText = ""
    var setAsCap: Boolean = false

    fun updateTheGrades(grades: Grades?) {
        this.grades = grades
        startSpellingTest()
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void event(ListChangedEvent event) {
    //        appData.setCurrentTest(event.listName);
    //        dataRepo.updateAppData(appData);
    //        Task.callInBackground(new StartATask()).onSuccessTask(new LoadListData()).onSuccess(new StartSpellingTest(), UI_THREAD_EXECUTOR);
    //    }

    fun noData() {
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

    fun showHint(hintAction: DialogInterface.OnClickListener) {
        view.showPopUp("Spell", grades!!.theCurrentGrade().theCurrentWeek().currentWord(), hintAction)
    }

    fun updateText(answerText: String) {
        this.answerText = answerText
        view.setTheAnswer(answerText)
        view.setCompletionProgress(grades!!.theCurrentGrade().theCurrentWeek().primaryProgress())
    }

    fun keyPressed(unicodeChar: Char) {
        if (unicodeChar == CAPS_KEY) {
            view.shiftKeyboard(true)
            setAsCap = true
            return
        }
        if (unicodeChar == HINT_KEY) {
            showHint(DoNothing())
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
            if (grades!!.theCurrentGrade().theCurrentWeek().isCorrectAnswer(answerText)) {
                view.stopCapturingTyping()
                view.showCheck()
                if (grades!!.theCurrentGrade().theCurrentWeek().atEnd()) {
                    view.popUpYouFinished()
                }
                else {
                    grades!!.theCurrentGrade().theCurrentWeek().nextWord()
                    showHint(HintClosed())
                }
            }
        }
    }

    fun startOver() {
        grades!!.theCurrentGrade().theCurrentWeek().setAtStart()
        updateText(answerText)
        showHint(HintClosed())
    }

    fun setListTitle() {
        view.setListTitle("Week ${grades!!.theCurrentGrade().theCurrentWeek().name}")
    }

    fun changeSpellingList() {
//        val map = HashMap<String, List<String>>()
//        map.put("Grade 6", grades.gr.grades!!.listNames())
//        view.showListChooser(asList("Grade 6"), map)
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

    inner class DoNothing : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
        }
    }

    companion object {
        val CAPS_KEY = (-1).toChar()
        val CLEAR_KEY = (-4).toChar()
        val HINT_KEY = (-5).toChar()
    }


}
