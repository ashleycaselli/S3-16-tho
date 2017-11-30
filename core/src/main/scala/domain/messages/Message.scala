package domain.messages

import domain._
import domain.messages.msgType.msgType
import play.api.libs.json._
import utils.EnumUtils

object msgType extends Enumeration {
    type msgType = Value
    val Clue = Value("ClueMsg")
    val Enrollment = Value("EnrollmentMsg")
    val Answer = Value("AnswerMsg")
    val Position = Value("PositionMsg")
    val Quiz = Value("QuizMsg")
    val State = Value("StateMsg")
    val Poi = Value("PoiMsg")
    val TreasureHunt = Value("TreasureHuntMsg")
    val ListTHs = Value("ListTHsMsg")
    val ListPOIs = Value("ListPOIsMsg")
    val Login = Value("LoginMsg")
    val Registration = Value("RegistrationMsg")
    val Unsubscription = Value("UnsubscriptionMsg")

    implicit val enumReads: Reads[msgType] = EnumUtils.enumReads(msgType)

    implicit def enumWrites: Writes[msgType] = EnumUtils.enumWrites
}

/**
  * A Message is an entity that could be spread through a communication channel.
  */
trait Message extends Serializable {

    def messageType: msgType

    /**
      * Property to get the message's sender
      *
      * @return a String containing the sender's id
      */
    def sender: String

    /**
      * Property to get the message's payload
      *
      * @return contains a String that represents the payload
      */
    def payload: String

}

/**
  * Object that use the factory pattern to create a concrete message
  */
object Message {

    def apply(messageType: msgType, sender: String, entity: String): Message = messageType match {
        case msgType.Clue => ClueMsgImpl(sender, entity)
        case msgType.Quiz => QuizMsgImpl(sender, entity)
        case msgType.Poi => PoiMsgImpl(sender, entity)
        case msgType.Answer => AnswerMsgImpl(sender, entity)
        case msgType.Enrollment => EnrollmentMsgImpl(sender, entity)
        case msgType.Position => PositionMsgImpl(sender, entity)
        case msgType.State => StateMsgImpl(sender, entity)
        case msgType.TreasureHunt => TreasureHuntMsgImpl(sender, entity)
        case msgType.ListTHs => ListTHsMsgImpl(sender, entity)
        case msgType.ListPOIs => ListPOIsMsgImpl(sender, entity)
        case msgType.Login => LoginMsgImpl(sender, entity)
        case msgType.Registration => RegistrationMsgImpl(sender, entity)
        case msgType.Unsubscription => UnsubscriptionMsgImpl(sender, entity)
        case _ => throw NoMsgDefinedException(s"No message defined for $entity class")
    }

    import play.api.libs.functional.syntax._

    implicit val messageReads: Reads[Message] = (
        (JsPath \ "messageType").read[msgType] and
            (JsPath \ "sender").read[String] and
            (JsPath \ "payload").read[String]
        ) (Message.apply _)

}