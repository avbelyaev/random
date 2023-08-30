package com.avbelyaev.crawler.utils

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URI

class Parser(private val scopedToDomain: String) {

    fun extractLinks(document: Document): List<String> {
        document.select("a[href*=#]").remove() // remove links starting with `#` e.g. https://monzo.com#mainContent
        return document.select("a").asSequence()
            .map { sanitizeUrl(it) }
            .filter { it.startsWith("http") && !it.endsWith(".pdf") }
            .filter { getDomainName(it) == scopedToDomain }
//            .filter { it.contains("/legal/") }
            .distinct()
            .toList()
    }

    private fun sanitizeUrl(htmlLink: Element): String = htmlLink.absUrl("href").removeSuffix("/")

    private fun getDomainName(url: String): String = URI(url).host.removePrefix("www.")
}
