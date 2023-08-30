package com.avbelyaev.crawler

import com.avbelyaev.crawler.application.Crawler
import com.avbelyaev.crawler.domain.Link
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
        val baz = Link("$WIREMOCK_HOST/baz")
        val foo2 = Link("$WIREMOCK_HOST/foo-2")
        val foo = Link("$WIREMOCK_HOST/foo", children = mutableSetOf(baz, foo2))
        val bar = Link("$WIREMOCK_HOST/bar", children = mutableSetOf(baz))
        val about = Link("$WIREMOCK_HOST/about")
        val root = Link("$WIREMOCK_HOST/index.html", children = mutableSetOf(foo, bar, about))

        // and
        val crawler = Crawler(workersNum = 2, webclient, parser)
        val seed = Link(url = "$WIREMOCK_HOST/index.html")

        // when
        crawler.crawl(seed)
        println(seed.asTree())

        // then
        Assertions.assertEquals(root, seed)
    }

    @Test
    fun shouldCrawlCyclicLinks() {
        // given
        val crawler = Crawler(workersNum = 2, webclient, parser)
        val seed = Link(url = "$WIREMOCK_HOST/cycle/cycle-a.html")

        // when
        crawler.crawl(seed)
        println(seed.asTree())

        // then
        Assertions.assertEquals("$WIREMOCK_HOST/cycle/cycle-a.html", seed.url)
        Assertions.assertEquals("$WIREMOCK_HOST/cycle/cycle-b.html", seed.children.toList().first().url)
        Assertions.assertEquals("$WIREMOCK_HOST/cycle/cycle-c.html", seed.children.toList().first().children.toList().first().url)
        Assertions.assertEquals("$WIREMOCK_HOST/cycle/cycle-a.html", seed.children.toList().first().children.toList().first().children.first().url)
        Assertions.assertTrue(seed.children.toList().first().children.toList().first().children.first().children.isEmpty())
    }

    @Test
    fun shouldCrawlIfNoInternalLinksPresent() {
        // given
        val crawler = Crawler(workersNum = 2, webclient, parser)
        val seed = Link(url = "$WIREMOCK_HOST/nolinks")

        // when
        crawler.crawl(seed)
        println(seed.asTree())

        // then
        Assertions.assertTrue(seed.children.isEmpty())
    }

}
