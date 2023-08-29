package com.avbelyaev.crawler.domain.model.website

data class WebsiteMap(
    val root: Node
) {
    override fun toString(): String {
        return "Website map:\n${root.asTree(1)}"
    }
}
