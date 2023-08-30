package com.avbelyaev.crawler

import com.avbelyaev.crawler.application.Crawler
import com.avbelyaev.crawler.domain.Node
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.utils.Parser
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9997)
class CrawlerE2ETests {

    companion object {
        private const val WIREMOCK_HOST = "http://localhost:9997"
    }

    private val webclient = WebClient(3)
    private val parser = Parser(scopedToDomain = "localhost")

    private val bazPage = Node("$WIREMOCK_HOST/baz")
    private val foo2Page = Node("$WIREMOCK_HOST/foo-2")
    private val fooPage = Node("$WIREMOCK_HOST/foo", children = mutableSetOf(bazPage, foo2Page))
    private val barPage = Node("$WIREMOCK_HOST/bar", children = mutableSetOf(bazPage))
    private val aboutPage = Node("$WIREMOCK_HOST/about")
    private val rootPage = Node("$WIREMOCK_HOST/index.html", children = mutableSetOf(fooPage, barPage, aboutPage))

    @Test
    fun shouldCrawlAllLinks() {
        // given
        val crawler = Crawler(workersNum = 2, webclient, parser)
        val seed = Node(url = "$WIREMOCK_HOST/index.html")

        // when
        crawler.crawl(seed)
        println(seed.asTree())

        // then
        Assertions.assertEquals(rootPage, seed)
    }

}
