package com.cultureamp.kotlin_kafka_http_sink_connector

import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.common.config.ConfigDef

class KafkaHttpSinkConnector : SinkConnector {
    constructor() : super()

    override fun start(props: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun config(): ConfigDef {
        TODO("Not yet implemented")
    }

    override fun taskClass(): Class<out Task> {
        TODO("Not yet implemented")
    }

    override fun taskConfigs(maxTasks: Int): MutableList<MutableMap<String, String>> {
        TODO("Not yet implemented")
    }

    override fun version(): String {
        TODO("Not yet implemented")
    }
}

class HttpSinkTask : SinkTask {
    constructor() : super()

    override fun start(props: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun put(records: MutableCollection<SinkRecord>?) {
        TODO("Not yet implemented")
    }

    override fun version(): String {
        TODO("Not yet implemented")
    }
}

//class KafkaHttpSinkConnectorConfig : AbstractConfig {
//    constructor(config: ConfigDef, parsedConfig: Map<String, String>) : super(config, parsedConfig)
//    constructor(parsedConfig: Map<String, String>) : super(defaultConfig(), parsedConfig)
//}
//
//fun defaultConfig() : ConfigDef {
//    return TODO("Not yet implemented")
//}

fun main() {
    println("Hello");
}