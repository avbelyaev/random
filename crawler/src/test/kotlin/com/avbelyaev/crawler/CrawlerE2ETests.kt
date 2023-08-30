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

    @Test
    fun shouldCrawlAllLinks() {
        // given
        val bazPage = Node("$WIREMOCK_HOST/baz")
        val foo2Page = Node("$WIREMOCK_HOST/foo-2")
        val fooPage = Node("$WIREMOCK_HOST/foo", children = mutableSetOf(bazPage, foo2Page))
        val barPage = Node("$WIREMOCK_HOST/bar", children = mutableSetOf(bazPage))
        val aboutPage = Node("$WIREMOCK_HOST/about")
        val rootPage = Node("$WIREMOCK_HOST/index.html", children = mutableSetOf(fooPage, barPage, aboutPage))

        // and
        val crawler = Crawler(workersNum = 2, webclient, parser)
        val seed = Node(url = "$WIREMOCK_HOST/index.html")

        // when
        crawler.crawl(seed)
        println(seed.asTree())

        // then
        Assertions.assertEquals(rootPage, seed)
    }

    @Test
    fun shouldCrawlCyclicLinks() {
        // given
        val cyclic =
            Node(url = "$WIREMOCK_HOST/cycle/cycle-a.html",
                children = mutableSetOf(
                    Node(url = "$WIREMOCK_HOST/cycle/cycle-b.html",
                        children = mutableSetOf(
                            Node(url = "$WIREMOCK_HOST/cycle/cycle-c.html",
                                children = mutableSetOf())
                        ))
                ))

        // and
        val crawler = Crawler(workersNum = 2, webclient, parser)
        val seed = Node(url = "$WIREMOCK_HOST/cycle/cycle-a.html")

        // when
        crawler.crawl(seed)
        println(seed.asTree())

        // then
        Assertions.assertEquals(cyclic, seed)
    }

    @Test
    fun shouldCrawlIfNoInternalLinksPresent() {
        // given
        val crawler = Crawler(workersNum = 2, webclient, parser)
        val seed = Node(url = "$WIREMOCK_HOST/nolinks")

        // when
        crawler.crawl(seed)
        println(seed.asTree())

        // then
        Assertions.assertTrue(seed.children.isEmpty())
    }

}
