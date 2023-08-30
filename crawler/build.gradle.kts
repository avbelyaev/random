plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "com.avbelyaev"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    implementation("org.jsoup:jsoup:1.16.1")

    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.7")

    testImplementation(kotlin("test"))
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.35.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("com.avbelyaev.crawler.AppKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.avbelyaev.crawler.AppKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
