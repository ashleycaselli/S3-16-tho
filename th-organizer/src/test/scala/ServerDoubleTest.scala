import com.rabbitmq.client._

class ServerDoubleTest {

    private val RPC_QUEUE_NAME: String = "rpc_queue";

    new Thread(() => {

        val factory = new ConnectionFactory
        factory setHost "localhost"

        val connection = factory newConnection
        val channel: Channel = connection createChannel()
        channel queueDeclare(RPC_QUEUE_NAME, false, false, false, null)
        channel basicQos 1

        println(" [ServerDoubleTest] Awaiting RPC requests")

        val consumer: Consumer = new DefaultConsumer(channel) {
            override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
                val replyProps = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build
                var response = ""
                val message: String = new String(body, "UTF-8")
                println(" [ServerDoubleTest] echo(" + message + ")")
                response += echo(message)
                channel basicPublish("", properties.getReplyTo, replyProps, response.getBytes("UTF-8"))
                channel basicAck(envelope.getDeliveryTag, false)
            }
        }

        channel basicConsume(RPC_QUEUE_NAME, false, consumer)


    }).start()

    def echo(s: String): String = {
        println(s" [ServerDoubleTest] Echo - Received text: $s")
        s
    }

}
