package model

import java.util.UUID

import com.rabbitmq.client._
import com.typesafe.scalalogging.Logger

/**
  * Entity able to handle the data exchange to/from data model
  */
sealed trait Broker {

    def call(name: String): String

    def send(msg: String): Unit

    def host(hostAddress: String): Unit

}

class ModelBroker extends Broker {

    private val EXCHANGE_NAME = "organizer-message"

    private var connection: Connection = _
    private var channelRPC: Channel = _
    private var channelPS: Channel = _
    private val requestQueueName: String = "rpc_queue"
    private var replyQueueName: String = _
    private var consumer: QueueingConsumer = _

    private val logger = Logger[ModelBroker]

    private val factory = new ConnectionFactory
    this host "52.14.140.101"
    factory.setUsername("test")
    factory.setPassword("test")
    connection = factory newConnection()
    channelRPC = connection createChannel()
    replyQueueName = channelRPC queueDeclare() getQueue

    consumer = new QueueingConsumer(channelRPC)
    channelRPC.basicConsume(replyQueueName, true, consumer)

    channelPS = connection.createChannel()
    channelPS exchangeDeclare(EXCHANGE_NAME, "fanout")

    override def send(msg: String): Unit = {
        channelPS.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes)
        logger info s"Message $msg sent!"
    }

    def call(msg: String): String = {
        logger info s"Calling.... $msg"
        val corrId: String = UUID randomUUID() toString
        val props: AMQP.BasicProperties = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build
        channelRPC basicPublish("", requestQueueName, props, msg.getBytes("UTF-8"))

        var response: String = null
        var found: Boolean = false
        while (!found) {
            val delivery: QueueingConsumer.Delivery = consumer.nextDelivery()
            if (delivery.getProperties.getCorrelationId == corrId) {
                response = new String(delivery.getBody, "UTF-8")
                found = true
            }
        }
        logger info s"Response received: $response"
        response
    }

    def close(): Unit = {
        channelPS close()
        channelRPC close()
        logger info "Channels closed"
        connection close()
        logger info "Connection closed"
    }

    override def host(hostAddress: String): Unit = factory setHost hostAddress

}

object ModelBroker {
    def apply: ModelBroker = new ModelBroker
}
