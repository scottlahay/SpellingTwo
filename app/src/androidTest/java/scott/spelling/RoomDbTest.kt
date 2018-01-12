package scott.spelling

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import scott.spelling.model.Grade
import scott.spelling.model.Week
import scott.spelling.model.Word
import scott.spelling.system.FirebaseStockTheDatabase
import scott.spelling.system.SpellingDb
import scott.spelling.system.WordDao

class RoomDbTest {

    lateinit var db: SpellingDb
    lateinit var word1: Word
    lateinit var word2: Word
    lateinit var dao: WordDao
    lateinit var week: Week
    lateinit var grade: Grade
    @Before
    fun before() {
        word1 = Word("1", "2", 1, "dasWord")
        word2 = Word("3", "4", 1, "dasSecondWord")
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), SpellingDb::class.java).build()
        dao = db.wordDao()
        week = FirebaseStockTheDatabase.G5_week0
        grade = FirebaseStockTheDatabase.grade_5
        grade.sortWeeksByIndex()
    }

    @After
    fun after() = db.close()

    @Test
    fun basicInsertWorks() {
        dao.insert(word1)
        assertTrue(dao.all().contains(word1))
    }

    @Test
    fun insertAndSelectAWeekFromTheDb() {
        db.insertWeek(week)
        db.insertWeek(FirebaseStockTheDatabase.G5_week1)
        assertEquals(week, db.retrieveWeek(week.grade, week.week))
    }

    @Test
    fun getTheWeekNamesForAGrade() {
        db.insertWeek(week)
        val expected = listOf("0")
        assertEquals(expected, db.weeksForAGrade(week.grade))
    }

    @Test
    fun insertAndSelectAGradeFromTheDb() {
        grade.weeks.forEach { db.insertWeek(it) }
        assertEquals(grade, db.gradeFor(grade.grade))
    }

}