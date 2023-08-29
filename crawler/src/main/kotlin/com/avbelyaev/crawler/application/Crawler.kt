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
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


class Crawler(
    private val workersNum: Int,
    private val webClient: WebClient,
    private val parser: Parser
) {

    private val log = KotlinLogging.logger {}

    private val pending = AtomicInteger()
    private val visited = ConcurrentHashMap.newKeySet<String>()

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

                    log.debug { "Worker $i started on ${task.node.url}" }

                    val nextTasks = task.execute()
                    pending.decrementAndGet()

//                    ensureActive()
                    sendNextTasks(nextTasks, workersScope)

                    if (pending.get() == 0 || visited.size > 20) {
                        log.debug { "closing" }
                        tasks.close()
                        this.cancel()
                    }
                }
            }
            workers.add(worker)
        }
    }

    private fun sendNextTasks(nextTasks: List<Task>, scope: CoroutineScope) {
        for (nextTask in nextTasks) {
//            if (!tasks.isClosedForSend) {
//            scope.ensureActive()
                scope.launch(Dispatchers.Default) {
                    tasks.trySend(nextTask)
                    pending.incrementAndGet()
                }
//            }
        }
    }


    inner class Task(val node: Node) {

        suspend fun execute(): List<Task> {
            delay(1000L)                                                    // TODO throttle requests
            return try {
                val document = webClient.fetchDocument(node.url)

                visited.add(node.url)

                val links = parser.extractLinks(document, visited).map { Node(it) }
                node.children.addAll(links)
                log.debug { "Done ${node.url}. Found ${links.size} new links. Visited ${visited.size}. Pending ${pending.get()}" }
                links.map { Task(it) }

            } catch (e: Exception) {                                                // TODO catch properly
                log.error { "Could not crawl ${node.url}. Reason: ${e.message}" }
                listOf()
            }
        }
    }
}
