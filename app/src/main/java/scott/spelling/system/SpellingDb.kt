package scott.spelling.system

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.jetbrains.anko.collections.forEachWithIndex
import scott.spelling.model.Grade
import scott.spelling.model.Week
import scott.spelling.model.Word

@Database(entities = [Word::class], version = 1)
abstract class SpellingDb : RoomDatabase() {

    abstract fun wordDao(): WordDao

    fun insertWeek(week: Week) {
        week.words.forEachWithIndex { i, it -> wordDao().insert(Word(week.grade, week.week, i, it)) }
    }

    fun retrieveWeek(grade: String, week: String): Week {
        val temp = Week(grade, week)
        temp.addAll(wordDao().week(grade, week))
        return temp
    }

    fun weeksForAGrade(grade: String): MutableList<String> {
        val actual = wordDao().weeksForGrade(grade)
        val actualAsStrings = mutableListOf<String>()
        actual.forEach({ actualAsStrings.add(it.week) })
        return actualAsStrings
    }

    fun gradeFor(grade: String): Grade {
        val temp = Grade(grade)
        weeksForAGrade(grade).forEach { temp.add(retrieveWeek(grade, it)) }
        temp.sortWeeksByIndex()
        return temp
    }


}

