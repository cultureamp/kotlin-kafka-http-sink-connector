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
}