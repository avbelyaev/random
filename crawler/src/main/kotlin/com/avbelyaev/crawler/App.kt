package com.avbelyaev.crawler

import com.avbelyaev.crawler.application.Crawler
import com.avbelyaev.crawler.domain.Link
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.utils.Parser
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody


val DEFAULT_DOMAIN = "my.website.com"
val DEFAULT_URL = "https://$DEFAULT_DOMAIN"

class Args(parser: ArgParser) {
    val url by parser.storing("seed url for crawling. Default: $DEFAULT_URL").default(DEFAULT_URL)
    val domain by parser.storing("scope crawling to this domain only. Default: $DEFAULT_DOMAIN").default(DEFAULT_DOMAIN)
    val workers by parser.storing("number of workers. Default: 3") { toInt() }.default(3)
}

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::Args).run {

        val parser = Parser(scopedToDomain = this.domain)
        val webClient = WebClient(requestTimeoutSec = 5)
        val crawler = Crawler(workersNum = this.workers, webClient, parser)

        val seed = Link(this.url)

        crawler.crawl(seed)

        println(seed.asTree())
    }
}

