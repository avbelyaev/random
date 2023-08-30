package com.avbelyaev.crawler.application

import com.avbelyaev.crawler.domain.Node
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.utils.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger


class Crawler(
    private val workersNum: Int,
    private val webClient: WebClient,
    private val parser: Parser
) {

    private val log = KotlinLogging.logger {}

    private val pending = AtomicInteger()
    private val seen = mutableSetOf<String>()
    private val mutex = Mutex()

    private val tasks = Channel<Task>(capacity = Channel.UNLIMITED)         // TODO limit queue size
    private val workers = mutableListOf<Job>()
    private val workersScope = CoroutineScope(Dispatchers.Unconfined)

    fun crawl(seed: Node) = runBlocking {
        log.debug { "Starting $workersNum workers" }
        workersScope.launch {
            startWorkers()
        }

        log.info { "Crawling $seed" }
        enqueueNext(listOf(seed), this)

        workers.joinAll()
        tasks.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun startWorkers() = coroutineScope {
        repeat(workersNum) { i ->
            val worker = launch {
                while (isActive) {
                    val task = tasks.receive()

                    log.debug { "Worker $i started on $task" }
                    val next = task.execute()
                    pending.decrementAndGet()
                    log.debug { "Worker $i finished $task. Seen ${seen.size}" }
                    log.debug { ">pending ${pending.get()}" }

                    for (nxt in next) {
                        tasks.send(Task(nxt))
                        pending.incrementAndGet()
                    }
                    log.debug { ">>pending ${pending.get()}" }

                    log.debug { ">>>pending ${pending.get()}" }

                    if (tasks.isEmpty && pending.get() <= 0) {
                        log.debug { "Stopping workers" }
                        workersScope.cancel()
                    }
                }
            }
            workers.add(worker)
        }
    }

    private fun enqueueNext(next: List<Node>, scope: CoroutineScope) {

        scope.launch(Dispatchers.Default) {
            for (nxt in next) {
                tasks.send(Task(nxt))
                pending.incrementAndGet()
            }
        }
    }


    inner class Task(private val node: Node) {

        suspend fun execute(): List<Node> {
            delay(1000L)                                                    // TODO throttle requests
            return try {
                val document = webClient.fetchDocument(node.url)
                val links = parser.extractLinks(document).map { Node(it) }
                node.children.addAll(links)
                log.debug { ">>> links from ${node.url}: $links" }
                mutex.withLock {
                    val nextLinks = links.filter { !seen.contains(it.url) }
                    seen.addAll(links.map { it.url })
                    return nextLinks
                }

            } catch (e: Exception) {                                                // TODO catch properly
                log.error { "Could not crawl ${node.url}. Reason: $e" }
                listOf()
            }
        }

        override fun toString(): String = node.url
    }
}
