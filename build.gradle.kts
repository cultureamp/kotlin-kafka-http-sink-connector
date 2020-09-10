import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.4.0"
}
group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation("org.apache.kafka:connect-api:2.6.0")
                implementation("org.apache.kafka:kafka-clients:2.6.0")
                implementation("com.github.kittinunf.fuel:fuel:2.2.3")
                implementation("com.github.kittinunf.result:result:3.1.0")
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.0")
            }
        }
        val test by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.cultureamp.kotlin_kafka_http_sink_connector.MainKt"
    }
    // We want a "fat jar" with all dependencies bundled in.
    from(configurations.compileClasspath.map { config -> config.map { if (it.isDirectory) it else zipTree(it) } })
}
