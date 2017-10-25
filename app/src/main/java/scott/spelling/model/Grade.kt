package scott.spelling.model

import com.google.firebase.database.Exclude

class Grade(val name: String = "", val weeks: List<Week> = ArrayList<Week>()) {

    @get:Exclude
    var currentWeek: Int = 0

    fun theCurrentWeek() = weeks[currentWeek]

    fun listNames(): MutableList<String> {
        val names = mutableListOf<String>()
        weeks.forEach { names.add(it.name) }
        return names
    }

    fun empty() = weeks.isEmpty()

    fun findList(listName: String): Week? {
        weeks.forEach {
            if (listName == it.name) {
                return it
            }
        }
        return null
    }

}