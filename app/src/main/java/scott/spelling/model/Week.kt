package scott.spelling.model

import com.google.firebase.database.Exclude
import java.util.*

data class Week(var name: String = "", var words: MutableList<String> = ArrayList()) {

    @get:Exclude
    var current: Int = 0

    constructor(id: String, vararg words: String) : this(id, Utils.asList(*words))

    fun nextWord() {
        if (atEnd()) {
            setAtStart()
        }
        else {
            current++
        }
    }

    fun replace(newWords: List<String>) {
        words.clear()
        words.addAll(newWords)
    }

    fun lastIndex(): Int {
        return size() - 1
    }

    fun setAtEnd() {
        current = lastIndex()
    }

    fun atEnd(): Boolean {
        return current == lastIndex()
    }

    fun setAtStart() {
        current = 0
    }

    fun atStart(): Boolean {
        return current == 0
    }

    operator fun contains(word: String): Boolean {
        return words.contains(word)
    }

    fun isCorrectAnswer(answer: String): Boolean {
        return currentWord() == answer
    }

    fun add(word: String) {
        words.add(word.trim { it <= ' ' })
    }

    fun size(): Int {
        return words.size
    }

    fun currentWord(): String {
        return words[current]
    }

    fun primaryProgress(): Int {
        return calcProgress(current)
    }

    fun calcProgress(index: Int): Int {
        return (index.toDouble() / size().toDouble() * 100).toInt()
    }

    fun reset() {
        current = 0
    }
}
