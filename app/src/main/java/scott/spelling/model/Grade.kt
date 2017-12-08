package scott.spelling.model

import com.google.firebase.database.Exclude

class Grade(val name: String = "", val weeks: List<Week> = ArrayList<Week>()) {

    @get:Exclude
    var current: Int = 0

    fun currentWeek() = weeks[current]

    fun weekNames(): ArrayList<String> {
        val names = ArrayList<String>()
        weeks.forEach { names.add(it.name) }
        return names
    }

    fun empty() = weeks.isEmpty()

    fun changeWeek(name: String) {
        weeks.forEachIndexed { index, week -> if (week.name == name) current = index }
    }

}