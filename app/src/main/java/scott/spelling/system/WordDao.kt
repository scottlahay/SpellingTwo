package scott.spelling.system

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import scott.spelling.model.Word

@Dao
interface WordDao {

    @Insert
    fun insert(word: Word)

    @Query("Select * from Word")
    fun all(): MutableList<Word>

    @Query("Select * from Word where grade = :grade and week = :week ")
    fun week(grade: String, week: String): MutableList<Word>

    @Query("Select distinct week from Word where grade = :grade")
    fun weeksForGrade(grade: String): List<WeekColumn>
}

class WeekColumn(@ColumnInfo(name = "week") var week: String)