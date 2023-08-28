package com.avbelyaev

import kotlinx.coroutines.runBlocking


val MONZO = "monzo.com"
val MONZO_URL = "https://$MONZO"

fun main(args: Array<String>) = runBlocking {
    println("Program arguments: ${args.joinToString()}")

    val parser = Parser(scopedToDomain = MONZO)
    val webClient = WebClient(requestTimeoutSec = 5)
    println("x")
    val crawler = Crawler(workersNum = 3, webClient, parser)
    println("y")
    val seed = listOf(
        Node(MONZO_URL)
    )
    crawler.crawl(seed)

}

