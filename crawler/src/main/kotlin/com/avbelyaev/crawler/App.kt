package com.avbelyaev.crawler

import com.avbelyaev.crawler.utils.Parser
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.application.Crawler
import com.avbelyaev.crawler.domain.model.website.Node
import com.avbelyaev.crawler.domain.model.website.WebsiteMap
import kotlinx.coroutines.runBlocking


val MONZO = "monzo.com"
val MONZO_URL = "https://$MONZO"

fun main(args: Array<String>) = runBlocking {
    println("Program arguments: ${args.joinToString()}")

    val parser = Parser(scopedToDomain = MONZO)
    val webClient = WebClient(requestTimeoutSec = 5)
    val crawler = Crawler(workersNum = 10, webClient, parser)

    val seed = Node(MONZO_URL)

    crawler.crawl(seed)

    val website = WebsiteMap(seed)
    println(website)
}

