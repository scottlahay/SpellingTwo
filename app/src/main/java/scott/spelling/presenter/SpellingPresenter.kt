package scott.spelling.presenter

import scott.spelling.model.Grades
import scott.spelling.view.MainActivity

class SpellingPresenter(var view: MainActivity) {
    var grades: Grades? = null
    var answerText = ""
    var setAsCap: Boolean = false

    fun updateTheGrades(grades: Grades?) {
        this.grades = grades
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void event(ListChangedEvent event) {
    //        appData.setCurrentTest(event.listName);
    //        dataRepo.updateAppData(appData);
    //        Task.callInBackground(new StartATask()).onSuccessTask(new LoadListData()).onSuccess(new StartSpellingTest(), UI_THREAD_EXECUTOR);
    //    }

    fun noData() {
//        view.hideProgress()
        val message = "Sorry, the first time you use this app you need to have an internet connection. Please connect to the Internet"
        view.showPopUp(message, "", view.closeApp)
    }


}
