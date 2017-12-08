package scott.spelling.presenter

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import scott.spelling.model.Grades
import scott.spelling.system.FirebaseRepository

enum class MainPage { ABOUT, TEST, LANDING }

class SpellingViewModel : ViewModel() {

    var grades: Grades = Grades()
        set(value) {
            field = value
            currentInfo()
        }

    var setAsCap = false

    val landingGradeTitle: MutableLiveData<String> = MutableLiveData()
    val landingWeekTitle: MutableLiveData<String> = MutableLiveData()
    val appTitle: MutableLiveData<String> = MutableLiveData()
    val showPage: MutableLiveData<MainPage> = MutableLiveData()
    val progressVisible: MutableLiveData<Boolean> = MutableLiveData()
    val answerText: MutableLiveData<String> = MutableLiveData()
    val showCheckMark: MutableLiveData<Boolean> = MutableLiveData()
    val captureTyping: MutableLiveData<Boolean> = MutableLiveData()
    val completionProgress: MutableLiveData<Int> = MutableLiveData()
    val showPopup: MutableLiveData<BasicDialogDetails> = MutableLiveData()
    val shiftKeyboard: MutableLiveData<Boolean> = MutableLiveData()
    val changeDrawerData: MutableLiveData<List<String>> = MutableLiveData()
    val selectGrade: MutableLiveData<List<String>> = MutableLiveData()
    val selectWeek: MutableLiveData<List<String>> = MutableLiveData()

    init {
        FirebaseRepository(this)
    }

    fun setGrade(name: String) {
        grades.changeGrade(name)
        FirebaseRepository.UserPreferences.grade = name
        currentInfo()
        goToLandingPage()
    }

    fun setWeek(name: String) {
        grades.changeWeek(name)
        FirebaseRepository.UserPreferences.week = name
        currentInfo()
        goToLandingPage()
    }

    fun currentInfo() {
        landingGradeTitle.value = "Grade ${grades.currentGrade().name}"
        landingWeekTitle.value = "Week ${grades.currentWeek().name}"
        changeDrawerData.value = listOf(grades.currentGrade().name, grades.currentWeek().name)
        appTitle.value = "Week ${grades.currentWeek().name}"
    }

    fun go() {
        appTitle.value = "Week ${grades.currentWeek().name}"
        showPage.value = MainPage.TEST
        progressVisible.value = false
        updateText("")
        showHint()
    }

    fun showHint() {
        showPopup.value = Hint(this, "Spell", grades.currentWord())
    }

    fun updateText(updatedText: String) {
        answerText.value = updatedText
        completionProgress.value = grades.currentGrade().currentWeek().primaryProgress()
    }

    fun startOver() {
        grades.currentGrade().currentWeek().setAtStart()
        updateText("")
        showHint()
    }

    fun keyPressed(unicodeChar: Char) {
        if (unicodeChar == CAPS_KEY) {
            shiftKeyboard.value = true
            setAsCap = true
            return
        }
        if (unicodeChar == HINT_KEY) {
            showPopup.value = BasicDialogDetails("Spell", grades.currentWord())
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
                shiftKeyboard.value = false
            }
            updateText("${answerText.value}$temp")
            if (grades.currentWeek().isCorrectAnswer(answerText.value!!)) {
                captureTyping.value = false
                showCheckMark.value = true
                if (grades.currentWeek().atEnd()) {
                    showPopup.value = Finished(this, "", "You Finished!")
                }
                else {
                    grades.currentWeek().nextWord()
                    showHint()

                }
            }
        }
    }

    companion object {
        val CAPS_KEY = (-1).toChar()
        val CLEAR_KEY = (-4).toChar()
        val HINT_KEY = (-5).toChar()
    }

    fun goToLandingPage() {
        grades.currentWeek().reset()
        showPage.value = MainPage.LANDING
    }

    fun launchGradeChanger() {
        selectGrade.value = grades.gradeNames()
    }

    fun launchWeekChanger() {
        selectWeek.value = grades.currentGrade().weekNames()
    }

    fun launchAboutPage() {
        showPage.value = MainPage.ABOUT
    }

}