package com.avbelyaev.crawler.domain

data class Link(
    val url: String,
    val children: MutableSet<Link> = mutableSetOf()
) {
    fun addChildren(children: List<Link>) = children

    fun asTree() = asTree(1)

    private fun asTree(indent: Int): String {
        val repr = StringBuilder("$url\n")
        for (child in children) {
            val line = ". ".repeat(indent) + child.asTree(indent + 1)
            repr.append(line)
        }
        return repr.toString()
    }
}
