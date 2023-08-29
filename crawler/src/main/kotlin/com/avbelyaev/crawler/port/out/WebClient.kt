package com.avbelyaev.crawler.port.out

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebClient(private val requestTimeoutSec: Int) {

    fun fetchDocument(url: String): Document {
        return Jsoup.connect(url)
            .timeout(requestTimeoutSec * 1000) // millis
            .get()
    }
}
