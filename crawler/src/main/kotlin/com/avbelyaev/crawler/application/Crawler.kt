package com.avbelyaev.crawler.application

import com.avbelyaev.crawler.domain.Link
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.utils.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
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
    private val enqueued = mutableSetOf<String>()
    private val mutex = Mutex()

    private val taskChannel = Channel<Task>(capacity = Channel.UNLIMITED)  // blocking queue for coroutines
    private val workers = mutableListOf<Job>()
    private val workersScope = CoroutineScope(Dispatchers.Unconfined)

    fun crawl(seed: Link) = runBlocking {
        pending.set(0)
        enqueued.clear()

        log.debug { "Starting $workersNum workers" }
        workersScope.launch {
            startWorkers()
        }

        log.info { "Crawling $seed" }
        enqueueNext(listOf(seed), this)

        workers.joinAll()       // wait for all coroutines to finish and close the channel
        taskChannel.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun startWorkers() = coroutineScope {
        repeat(workersNum) { i ->
            val worker = launch {
                while (isActive) {
                    val task = taskChannel.receive()

                    log.debug { "Worker $i started on $task" }
                    val nextLinks = task.execute()
                    pending.decrementAndGet()       // no matter what's the outcome, mark this as done
                    log.debug { "Worker $i finished $task. Found ${nextLinks.size} next links. Seen ${enqueued.size}" }

                    enqueueNext(nextLinks, workersScope)

                    if (taskChannel.isEmpty && pending.get() <= 0) {    // make sure work queue is empty and nothing in-flight
                        log.debug { "Stopping workers" }
                        workersScope.cancel()
                    }
                }
            }
            workers.add(worker)
        }
    }

    private fun enqueueNext(nextLinks: List<Link>, scope: CoroutineScope) {
        for (link in nextLinks) {
            scope.launch(Dispatchers.Default) {
                taskChannel.send(Task(link))
            }
            pending.incrementAndGet()
        }
    }


    inner class Task(private val link: Link) {

        suspend fun execute(): List<Link> {
//            delay(1000L)                                                    // TODO throttle requests
            return try {
                val document = webClient.fetchDocument(link.url)
                val links = parser.extractLinks(document)
                link.addChildren(links)

                mutex.withLock {
                    val nextLinks = links.filter { !enqueued.contains(it.url) }
                    enqueued.add(link.url)
                    enqueued.addAll(links.map { it.url })   // make sure we don't enqueue same links over and over, e.g. "About" from the footer
                    return nextLinks
                }

            } catch (e: Exception) {                                                // TODO catch properly
                log.error { "Could not crawl ${link.url}. Reason: $e" }
                listOf()
            }
        }

        override fun toString(): String = link.url
    }
}
