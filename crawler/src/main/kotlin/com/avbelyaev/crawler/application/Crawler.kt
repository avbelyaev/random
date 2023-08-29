package com.avbelyaev.crawler.application

import com.avbelyaev.crawler.domain.model.website.Node
import com.avbelyaev.crawler.port.out.WebClient
import com.avbelyaev.crawler.utils.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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

    private val tasks = Channel<Task>()
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
                while (isActive) {
                    val task = tasks.receive()

                    log.debug { "Worker $i started on ${task.node.url}" }

                    val nextTasks = task.execute()
                    pending.decrementAndGet()

                    sendNextTasks(nextTasks, workersScope)

                    if (pending.get() == 0 || visited.size > 5) {
                        log.debug { "closing" }
                        tasks.close()
                    }
                }
            }
            workers.add(worker)
        }
    }

    private fun sendNextTasks(nextTasks: List<Task>, scope: CoroutineScope) {
//        log.debug { "sending ${nextTasks.size} more" }
        for (nextTask in nextTasks) {
            scope.launch(Dispatchers.Default) {
                pending.incrementAndGet()
                tasks.trySend(nextTask)
            }
        }
    }


    inner class Task(val node: Node) {

        suspend fun execute(): List<Task> {
            delay(1000L)                                                    // TODO throttle requests
            return try {
//                log.info { "Starting ${node.url}" }
                val document = webClient.fetchDocument(node.url)

                visited.add(node.url)

                val links = parser.extractLinks(document, visited).map { Node(it) }
                node.children.addAll(links)
                log.debug { "Done ${node.url}. Found ${links.size} new links. Visited ${visited.size}. Pending ${pending.get()}" }
                links.map { Task(it) }

            } catch (e: Exception) {                                                // TODO catch properly
                log.error { "Could not crawl ${node.url}. Reason: ${e.message}" }
                listOf()

//            } finally {
//
            }
        }
    }
}
