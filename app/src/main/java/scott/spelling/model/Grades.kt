package scott.spelling.model

import com.google.firebase.database.Exclude

class Grades(val grades: List<Grade> = listOf()) {

    @get:Exclude
    var currentGrade: Int = 0

    fun theCurrentGrade() = grades[currentGrade]

}