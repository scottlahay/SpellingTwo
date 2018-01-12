package scott.spelling.model

import com.google.firebase.database.Exclude
import java.util.*

data class Week(var grade: String = "", var week: String = "", var words: MutableList<String> = ArrayList(25)) {

    @get:Exclude // from saving to firebase
    var current: Int = 0

    constructor(grade: String, week: String, vararg words: String) : this(grade, week, Utils.asList(*words))

    operator fun contains(word: String) = words.contains(word)
    fun nextWord() = if (atEnd()) reset() else current++
    fun lastIndex() = size() - 1
    fun atEnd() = current == lastIndex()
    fun isCorrectAnswer(answer: String) = currentWord() == answer
    fun add(word: String) = words.add(word.trim { it <= ' ' })
    fun size() = words.size
    fun currentWord() = words[current]
    fun primaryProgress() = calcProgress(current)
    fun addAll(words: MutableList<Word>) {
        words.sortBy { it.index }
        words.forEach { add(it.word) }
    }

    fun calcProgress(index: Int) = (index.toDouble() / size().toDouble() * 100).toInt()

    fun reset() {
        current = 0
    }

}
