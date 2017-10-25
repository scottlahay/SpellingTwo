package scott.spelling.presenter

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import scott.spelling.model.Grades
import scott.spelling.model.SpellingLists
import scott.spelling.system.FirebaseRepository

class SpellingViewModel : ViewModel() {

    var grades: MutableLiveData<Grades> = MutableLiveData()
    var spellingLists: MutableLiveData<SpellingLists> = MutableLiveData()

    init {
        FirebaseRepository(this)
    }
}