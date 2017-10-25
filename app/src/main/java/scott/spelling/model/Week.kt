package scott.spelling.model

import com.google.firebase.database.Exclude
import java.util.*

data class Week(var name: String = "", var words: MutableList<String> = ArrayList()) {

    @get:Exclude
    var currentWord: Int = 1

    constructor(id: String, vararg words: String) : this(id, Utils.asList(*words))

    fun nextWord() {
        if (atEnd()) {
            setAtStart()
        }
        else {
            currentWord++
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
        currentWord = lastIndex()
    }

    fun atEnd(): Boolean {
        return currentWord == lastIndex()
    }

    fun setAtStart() {
        currentWord = 0
    }

    fun atStart(): Boolean {
        return currentWord == 0
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
        return words[currentWord]
    }

    fun primaryProgress(): Int {
        return calcProgress(currentWord)
    }

    fun calcProgress(index: Int): Int {
        return (index.toDouble() / size().toDouble() * 100).toInt()
    }
}
