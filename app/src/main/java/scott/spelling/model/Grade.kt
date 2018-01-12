package scott.spelling.model

import com.google.firebase.database.Exclude

data class Grade(val grade: String = "", val weeks: MutableList<Week> = ArrayList<Week>()) {

    @get:Exclude // from firebase
    var current: Int = 0

    fun currentWeek() = weeks[current]
    fun changeWeek(name: String) = weeks.forEachIndexed { index, week -> if (week.week == name) current = index }

    fun weekNames(): ArrayList<String> {
        val names = ArrayList<String>()
        weeks.forEach { names.add(it.week) }
        return names
    }

    fun add(week: Week) = weeks.add(week)

    fun sortWeeksByIndex() = weeks.sortBy { it.week.toInt() }
}