package com.avbelyaev

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

    fun crawl(nodes: List<Node>) = runBlocking {
        workersScope.launch {
            startWorkers()
        }

        log.info { "Crawling $nodes" }
        sendNextTasks(nodes.map { Task(it.url) }, this)

        workers.joinAll()
    }

    private suspend fun startWorkers() = coroutineScope {
        repeat(workersNum) { i ->
            log.debug { "Starting worker $i" }

            val worker = launch {
                while (isActive) {
                    val task = tasks.receive()
                    val nextTasks = task.execute()

                    pending.decrementAndGet()

                    sendNextTasks(nextTasks, workersScope)

                    if (pending.get() == 0) {
                        tasks.close()
                    }
                }
            }
            workers.add(worker)
        }
    }

    private fun sendNextTasks(nextTasks: List<Task>, scope: CoroutineScope) {
        for (nextTask in nextTasks) {
            scope.launch(Dispatchers.Default) {
                pending.incrementAndGet()
                tasks.send(nextTask)
            }
        }
    }


    inner class Task(private val url: String) {

        suspend fun execute(): List<Task> {
            delay(2000L)                                    // TODO throttle
            return try {
                val document = webClient.fetchDocument(url)

                visited.add(url)

                val links = parser.extractLinks(document, visited).map { Task(it) }
                log.debug { "Done $url. Found ${links.size} new links. Visited ${visited.size}. Pending ${pending.get()}" }
                links

            } catch (e: Exception) {
                log.error { "Could not parse $url. Reason: ${e.message}" }
                listOf()
            }
        }
    }
}

data class Node(
    val url: String,
    val children: MutableSet<Node> = mutableSetOf()
)
