package receiver

import java.io.IOException

import com.rabbitmq.client._
import utils.RabbitInfo


trait Receiver {
    def startRecv: Unit
}

class ReceiverImpl extends Receiver {

    @throws[Exception]
    override def startRecv: Unit = {
        val factory = new ConnectionFactory
        factory.setHost(RabbitInfo.HOST)
        val connection = factory.newConnection
        val channel = connection.createChannel
        channel.queueDeclare(RabbitInfo.QUEUE_NAME, false, false, false, null)
        val consumer = new DefaultConsumer(channel) {
            @throws[IOException]
            override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
                channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, RabbitInfo.KO_RESPONSE.getBytes)
            }
        }
        channel.basicConsume(RabbitInfo.QUEUE_NAME, true, consumer)


        val channelPS = connection.createChannel
        channelPS.exchangeDeclare(RabbitInfo.EXCHANGE_NAME, "fanout")
        val queueName = channelPS.queueDeclare.getQueue
        channelPS.queueBind(queueName, RabbitInfo.EXCHANGE_NAME, "")
        val consumer2 = new DefaultConsumer(channelPS) {
            @throws[IOException]
            override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
                val data = new String(body, RabbitInfo.MESSAGE_ENCODING)

                /*implicit val msgReads = Json.reads[Message]
                
                val message = data.asInstanceOf[JsValue].as[Message]
                val sender = message.sender
                val mType = message.messageType
                if (mType == msgType.Answer) { // received when a team solves a quiz
                    val payload = data.asInstanceOf[JsValue].as[AnswerMsg].payload
                }
                if (mType == msgType.Clue) { // never received
                    val payload = data.asInstanceOf[JsValue].as[ClueMsg].payload
                }
                if (mType == msgType.Enrollment) { // received when a team require to join a th
                    val payload = data.asInstanceOf[JsValue].as[EnrollmentMsg].payload
                }
                if (mType == msgType.Position) { // received if player reach a position
                    val payload = data.asInstanceOf[JsValue].as[PositionMsg].payload
                }
                if (mType == msgType.Quiz) { // never received
                    val payload = data.asInstanceOf[JsValue].as[QuizMsg].payload
                }
                if (mType == msgType.State) { // received state needs to be changed
                    val payload = data.asInstanceOf[JsValue].as[StateMsg].payload
                }
                if (mType == msgType.Poi) { // received if organizer creates a new POI
                    val payload = data.asInstanceOf[JsValue].as[PoiMsg].payload
                    val poi = payload.asInstanceOf[JsValue].as[POI]
                    //TODO call function to add poi to database
                    //TODO attention: it contains name, clue, quiz and the ID of the TH in which is contained
                }*/
            }
        }
        channelPS.basicConsume(queueName, true, consumer2)
    }
}

object Server extends App {
    val receiver: Receiver = new ReceiverImpl
    receiver startRecv
}
