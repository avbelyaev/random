package com.avbelyaev.crawler.application

import com.avbelyaev.crawler.domain.model.website.Node
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.utils.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


class Crawler(
    private val workersNum: Int,
    private val webClient: WebClient,
    private val parser: Parser
) {

    private val log = KotlinLogging.logger {}

    private val pending = AtomicInteger()
    private val visited = mutableSetOf<String>()
    private val toVisit = mutableSetOf<String>()
    private val mutex = Mutex()

    private val tasks = Channel<Task>(capacity = Channel.UNLIMITED)         // TODO limit queue size
    private val workers = mutableListOf<Job>()
    private val workersScope = CoroutineScope(Dispatchers.Unconfined)

    fun crawl(seed: Node) = runBlocking {
        workersScope.launch {
            startWorkers()
        }

        log.info { "Crawling $seed" }
        sendNextTasks(listOf(Task(seed)), this)

        workers.joinAll()
    }

    private suspend fun startWorkers() = coroutineScope {
        repeat(workersNum) { i ->
            log.debug { "Starting worker $i" }

            val worker = launch {
                while (isActive && !tasks.isClosedForReceive) {
                    val task = tasks.receive()

                    log.debug { "Worker $i started on $task" }
                    val nextTasks = task.execute()
                    log.debug { "Worker $i finished $task. Visited ${visited.size}. Pending ${pending.get()}" }

                    sendNextTasks(nextTasks, workersScope)

                    pending.decrementAndGet()

                    if (pending.get() <= 0) {
                        tasks.close()
                        log.debug { "Stopping worker $i" }
                        this.cancel()
                    }
                }
            }
            workers.add(worker)
        }
    }

    private fun sendNextTasks(nextTasks: List<Task>, scope: CoroutineScope) {
        for (nextTask in nextTasks) {
            scope.launch(Dispatchers.Default) {
                tasks.send(nextTask)
                pending.incrementAndGet()
            }
        }
    }


    inner class Task(private val node: Node) {

        suspend fun execute(): List<Task> {
//            delay(1000L)                                                    // TODO throttle requests

            return try {
                val document = webClient.fetchDocument(node.url)
                val links = parser.extractLinks(document).map { Node(it) }
                node.children.addAll(links)

                mutex.withLock {
                    visited.add(node.url)

                    val retLinks = links.filter { !visited.contains(it.url) }
                        .filter { !toVisit.contains(it.url) }
                        .map { Task(it) }
                    toVisit.addAll(links.map { it.url })
                    return retLinks
                }

//                synchronized(visited) {
//                    log.debug { "> visited $visited. tovisit $toVisit" }

//                    toVisit.remove(node.url)
//                    toVisit.addAll(links.map { it.url })

//                    log.debug { "< visited $visited. tovisit $toVisit" }



//                    log.debug { "Done ${node.url}. Found ${links.size} new links. Visited ${visited.size}. Pending ${pending.get()}" }

//                    links.map { Task(it) }
//                }

            } catch (e: Exception) {                                                // TODO catch properly
                log.error { "Could not crawl ${node.url}. Reason: $e" }
                listOf()
            }
        }

        override fun toString(): String = node.url
    }
}
