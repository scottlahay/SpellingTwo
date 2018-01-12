package scott.spelling.model

import com.google.firebase.database.Exclude

data class Grades(val grades: List<Grade> = listOf()) {

    @get:Exclude
    var current: Int = 0

    fun currentGrade() = grades[current]
    fun currentWeek() = currentGrade().currentWeek()
    fun currentWord() = currentWeek().currentWord()
    fun changeGrade(name: String) = grades.forEachIndexed { index, grade -> if (grade.grade == name) current = index }
    fun changeWeek(name: String) = currentGrade().changeWeek(name)

    fun gradeNames(): ArrayList<String> {
        val temp = ArrayList<String>()
        grades.forEach { temp.add(it.grade) }
        return temp
    }


}