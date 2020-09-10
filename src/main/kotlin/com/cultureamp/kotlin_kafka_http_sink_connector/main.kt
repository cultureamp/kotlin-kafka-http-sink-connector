package com.cultureamp.kotlin_kafka_http_sink_connector

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.config.AbstractConfig
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.common.config.ConfigDef.Importance
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.sink.SinkConnector
import org.apache.kafka.connect.sink.SinkRecord
import org.apache.kafka.connect.sink.SinkTask


const val CONFIG_TOPIC = "topic"
const val CONFIG_ENDPOINT = "endpoint"
const val CONFIG_MAX_BATCH_SIZE = "max_batch_size"
const val DEFAULT_MAX_BATCH_SIZE = 20
const val CONNECTOR_VERSION = "1.0"

class KafkaHttpSinkConnector : SinkConnector {
    private var topic: String? = null
    private var endpoint: String? = null
    private var maxBatchSize: Int = DEFAULT_MAX_BATCH_SIZE

    constructor() : super()

    override fun start(props: MutableMap<String, String>?) {
        this.topic = props?.get(CONFIG_TOPIC)
        this.endpoint = props?.get(CONFIG_ENDPOINT)
        if (props?.contains(CONFIG_MAX_BATCH_SIZE) == true) {
            this.maxBatchSize = Integer.parseInt(props[CONFIG_MAX_BATCH_SIZE]);
        }
    }

    override fun stop() {
        // Probably nothing we have to do here.
    }

    override fun config(): ConfigDef {
        return ConfigDef()
            .define(CONFIG_ENDPOINT, ConfigDef.Type.STRING, null, Importance.HIGH, "Http Sink Endpoint URL")
            .define(CONFIG_TOPIC, ConfigDef.Type.STRING, null, Importance.HIGH, "Http Sink Topic Names")
            .define(CONFIG_MAX_BATCH_SIZE, ConfigDef.Type.INT, DEFAULT_MAX_BATCH_SIZE, Importance.HIGH, "Http Sink Batch Size")
    }

    override fun taskClass(): Class<out Task> {
        return HttpSinkTask::class.java
    }

    override fun taskConfigs(maxTasks: Int): MutableList<MutableMap<String, String>> {
        val config: MutableMap<String, String> = mutableMapOf()
        val topic = this.topic
        val endpoint = this.endpoint
        if (topic != null) {
            config[TOPICS_CONFIG] = topic;
        }
        if (endpoint != null) {
            config[CONFIG_ENDPOINT] = endpoint;
        }
        config[CONFIG_MAX_BATCH_SIZE] = "$maxBatchSize";
        return MutableList(maxTasks) { config }
    }

    override fun version(): String {
        return CONNECTOR_VERSION;
    }
}

class HttpSinkTask : SinkTask {
    private var topic: String? = null
    private var endpoint: String? = null
    private var maxBatchSize: Int = DEFAULT_MAX_BATCH_SIZE

    constructor() : super()

    override fun start(props: MutableMap<String, String>?) {
        this.topic = props?.get(CONFIG_TOPIC)
        this.endpoint = props?.get(CONFIG_ENDPOINT)
        if (props?.contains(CONFIG_MAX_BATCH_SIZE) == true) {
            this.maxBatchSize = Integer.parseInt(props[CONFIG_MAX_BATCH_SIZE]);
        }
    }

    override fun stop() {
        // Unless we start batching requests there's probably nothing we have to do here.
    }

    override fun put(records: MutableCollection<SinkRecord>?) {
        if (records == null) {
            return
        }
        println("Put records ${records.size}")
        println("Config $topic $endpoint $maxBatchSize")
        var dataToSend: String = ""
        for (record in records) {
            record.topic()
            record.kafkaPartition()
            record.kafkaOffset()
            record.timestamp()
            record.key()
            record.keySchema()
            record.value()
            record.valueSchema()
            println("----")
            println("key ${record.key()}")
            println("topic ${record.topic()}")
            println("value ${record.value()}")
            dataToSend = "${dataToSend}\n\nTopic ${record.topic()} Key ${record.key()} Value ${record.value()}"
        }
        if (dataToSend.isNotEmpty()) {
            makeHttpRequest(this.endpoint, dataToSend)
        }
    }

    private fun makeHttpRequest(endpoint: String?, dataToSend: String) {
        if (endpoint == null) {
            return
        }
        // Despite the recommendation being that connector tasks generally run asynchronously,
        // if we want to guarantee partition ordering, we probably need to wait here.
        // At least that's what the confluent HTTP and Lambda sink connectors seem to do in "sync" mode.
        // Open question: how do we handle only blocking one partition if there's an error? Or does Kafka Connect do that for us?
        val (_, _, result) = endpoint
                .httpPost()
                .body(dataToSend)
                .responseString()
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                println("Exception $ex")
            }
            is Result.Success -> {
                val data = result.get()
                println("Data $data")
            }
        }
    }

    override fun flush(currentOffsets: MutableMap<TopicPartition, OffsetAndMetadata>?) {
        super.flush(currentOffsets)
        println("TODO: do we want to do anything to flush?")
    }

    override fun version(): String {
        return CONNECTOR_VERSION;
    }
}
