package scott.spelling.presenter

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.chibatching.kotpref.KotprefModel
import scott.spelling.BuildConfig
import scott.spelling.R
import scott.spelling.model.Grades
import scott.spelling.system.FirebaseRepository

enum class MainPage { ABOUT, TEST, LANDING }

class SpellingViewModel(application: Application) : AndroidViewModel(application) {

    var grades: Grades = Grades()
        set(value) {
            field = value
            field.changeGrade(UserPreferences.grade)
            field.changeWeek(UserPreferences.week)
            currentInfo()
        }

    fun addWordsToApp() {

    }

    var setAsCap = false
    var aboutPageTitle: String
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
    val selectGrade: MutableLiveData<List<String>> = MutableLiveData()
    val selectWeek: MutableLiveData<List<String>> = MutableLiveData()

    init {
        FirebaseRepository(this)
        aboutPageTitle = "${application.getString(R.string.app_name)} app - version ${BuildConfig.VERSION_CODE}"
    }

    fun setGrade(name: String) {
        grades.changeGrade(name)
        UserPreferences.grade = name
        currentInfo()
        goToLandingPage()
    }

    fun setWeek(name: String) {
        grades.changeWeek(name)
        UserPreferences.week = name
        currentInfo()
        goToLandingPage()
    }

    fun currentInfo() {  // this should be initializing any changes the user has made when the app loads
        landingGradeTitle.value = grades.currentGrade().grade
        landingWeekTitle.value = grades.currentWeek().week
        appTitle.value = "Week ${grades.currentWeek().week}"
    }

    fun showHint() {
        showPopup.value = Hint(this, "Spell", grades.currentWord())
    }

    fun updateText(updatedText: String) {
        answerText.value = updatedText
        completionProgress.value = grades.currentGrade().currentWeek().primaryProgress()
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

    fun goToTestPage() {
        appTitle.value = "Week ${grades.currentWeek().week}"
        showPage.value = MainPage.TEST
        progressVisible.value = false
        updateText("")
        showHint()
    }

    fun goToLandingPage() {
        appTitle.value = "Week ${grades.currentWeek().week}"
        grades.currentWeek().reset()
        showPage.value = MainPage.LANDING
    }

    fun goToAboutPage() {
        showPage.value = MainPage.ABOUT
        appTitle.value = aboutPageTitle
    }

    fun launchGradeChanger() {
        selectGrade.value = grades.gradeNames()
    }

    fun launchWeekChanger() {
        selectWeek.value = grades.currentGrade().weekNames()
    }

}

object UserPreferences : KotprefModel() {
    var grade by stringPref(default = "5")
    var week by stringPref(default = "1")
}
