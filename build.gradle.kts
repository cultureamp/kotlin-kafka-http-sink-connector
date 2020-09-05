plugins {
    kotlin("multiplatform") version "1.4.0"
}
group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    sourceSets {
        val jvmMain by getting
        val jvmTest by getting {
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