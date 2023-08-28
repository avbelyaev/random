package com.avbelyaev

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
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

//    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Unconfined)

    init {
        scope.launch{
            startWorkers()
        }

    }

    //    fun crawl(tasks: List<Task>) = runBlocking {
//        log.info { "Crawling $seeds" }
//
//        for (seed in seeds) {
//            pending.incrementAndGet()
//            tasks.send(Task(seed.url))
//        }
//
//        workers.joinAll()
//    }
    fun crawl(nodes: List<Node>) = runBlocking {
        log.info { "Crawling $nodes" }
        sendNextTasks(nodes.map { Task(it.url) }, this)
    }

    suspend fun startWorkers() = coroutineScope {

        repeat(workersNum) { i ->
            log.debug { "Launching worker $i" }
            val worker = launch {
                while (isActive) {
                    val task = tasks.receive()

                    log.debug { "Worker $i starts on ${task.url}" }

                    val nextTasks = task.execute()

                    pending.decrementAndGet()
                    visited.add(task.url)

//                    sendNextTasks(nextTasks, this)
                    for (nextTask in nextTasks) {
                        launch(Dispatchers.Default) {
                            pending.incrementAndGet()
                            tasks.send(nextTask)
                        }
                    }

                    if (pending.get() == 0) {
                        log.debug { "close" }
                        tasks.close()
                    }
                    println("isactive $isActive")
                }
                println("isactive2 $isActive")
            }
            workers.add(worker)
        }

        log.debug { "here" }
//        workers.joinAll()
    }

    private fun sendNextTasks(nextTasks: List<Task>, scope: CoroutineScope) {
        for (nextTask in nextTasks) {
            scope.launch(Dispatchers.Unconfined) {
                pending.incrementAndGet()
                tasks.send(nextTask)
            }
        }
    }


    inner class Task(val url: String) {

        suspend fun execute(): List<Task> {
//            delay(1000L)
            return try {
                log.debug { "fetching" }
                val document = webClient.fetchDocument(url)
                log.debug { "parsing " }
                val links = parser.extractLinks(document, visited)
                log.debug { "Done $url. Found ${links.size} new links. Visited ${visited.size}. ToProcess ${pending.get()}" }
                links.map { Task(it) }

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
