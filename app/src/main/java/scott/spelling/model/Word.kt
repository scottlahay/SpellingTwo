package scott.spelling.model

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["grade", "week", "word"])
data class Word(var grade: String = "", var week: String = "", var index: Int = 0, var word: String = "")