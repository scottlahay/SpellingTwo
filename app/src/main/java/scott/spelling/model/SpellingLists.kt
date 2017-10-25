package scott.spelling.model

open class SpellingLists(val name: String = "", val lists: List<SpellingList> = listOf()) {

    fun listNames(): MutableList<String> {
        val names = mutableListOf<String>()
        lists.forEach { names.add(it.id) }
        return names
    }

    fun empty(): Boolean {
        return lists.isEmpty()
    }

    fun findList(id: String): SpellingList? {
        lists.forEach {
            if (id == it.id) {
                return it
            }
        }
        return null
    }

}
