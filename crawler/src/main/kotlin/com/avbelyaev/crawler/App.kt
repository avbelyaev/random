package com.avbelyaev.crawler

import com.avbelyaev.crawler.application.Crawler
import com.avbelyaev.crawler.domain.Link
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.utils.Parser
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import kotlinx.coroutines.runBlocking


val MONZO = "monzo.com"
val MONZO_URL = "https://$MONZO"

class Args(parser: ArgParser) {
    val url by parser.storing("seed url for crawling. Default: $MONZO_URL").default(MONZO_URL)
    val domain by parser.storing("scope crawling to this domain only. Default: $MONZO").default(MONZO)
    val workers by parser.storing("number of workers") { toInt() }
}

fun main(args: Array<String>) = runBlocking {
    ArgParser(args).parseInto(::Args).let { args ->
        val parser = Parser(scopedToDomain = args.domain)
        val webClient = WebClient(requestTimeoutSec = 5)
        val crawler = Crawler(workersNum = args.workers, webClient, parser)

        val seed = Link(args.url)

        crawler.crawl(seed)

        println(seed.asTree())
    }
}

