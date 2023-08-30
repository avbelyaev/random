package com.avbelyaev.crawler.domain

data class Node(
    val url: String,
    val children: MutableSet<Node> = mutableSetOf()
) {
    fun asTree() = asTree(1)

    private fun asTree(indent: Int): String {
        val repr = StringBuilder("[ $url ]\n")
        for (child in children) {
            val line = ". ".repeat(indent) + child.asTree(indent + 1)
            repr.append(line)
        }
        return repr.toString()
    }
}
