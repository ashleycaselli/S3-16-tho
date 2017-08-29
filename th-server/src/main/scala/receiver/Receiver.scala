package receiver

import java.io.IOException

import com.rabbitmq.client._
import domain._
import domain.messages._
import domain.messages.msgType.msgType
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}
import utils.RabbitInfo

trait Receiver {

    def startRecv: Unit

    def getLastMessage(): String
}

class ReceiverImpl extends Receiver {

    private var lastMessage: String = null

    implicit val messageReads: Reads[Message] = (
      (JsPath \ "messageType").read[msgType] and
        (JsPath \ "sender").read[String] and
        (JsPath \ "payload").read[String]
      ) (Message.apply _)


    implicit val positionReads: Reads[Position] = (
      (JsPath \ "latitude").read[Double] and
        (JsPath \ "longitude").read[Double]
      ) (Position.apply _)

    implicit val quizReads: Reads[Quiz] = (
      (JsPath \ "question").read[String] and
        (JsPath \ "answer").read[String]
      ) (Quiz.apply _)

    implicit val clueReads: Reads[Clue] = (
      (JsPath \ "content").read[String].map(Clue.apply _)
      )

    implicit val poiReads: Reads[POI] = (
      (JsPath \ "name").read[String] and
        (JsPath \ "position").read[String] and
        (JsPath \ "quiz").read[String] and
        (JsPath \ "clue").read[String]
      ) (POI.apply _)


    @throws[Exception]
    override def startRecv: Unit = {
        println("Receiver started")
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
                lastMessage = data;
                val message = Json.parse(data).as[Message]
                val sender = message.sender
                val mType = message.messageType
                val payload = message.payload
                if (mType == msgType.Poi) { // received if organizer creates a new POI
                    var poi = Json.parse(payload).as[POI]
                    //TODO call function to add poi to database
                }
                if (mType == msgType.Clue) {
                    var clue = Json.parse(payload).as[Clue]
                    //TODO call database function
                }
                if (mType == msgType.Quiz) {
                    var quiz = Json.parse(payload).as[Quiz]
                    //TODO call database function
                }
                if (mType == msgType.Position) {
                    var position = Json.parse(payload).as[Position]
                    //TODO call database function
                }
            }
        }
        channelPS.basicConsume(queueName, true, consumer2)
    }

    override def getLastMessage(): String = {
        lastMessage
    }
}

object Server extends App {
    val receiver: Receiver = new ReceiverImpl
    receiver startRecv
}
