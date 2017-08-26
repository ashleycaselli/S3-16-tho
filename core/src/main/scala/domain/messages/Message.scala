package domain.messages

import domain._
import domain.messages.msgType.msgType

object msgType extends Enumeration {
    type msgType = Value
    val Clue = Value("ClueMsg")
    val Enrollment = Value("EnrollmentMsg")
    val Answer = Value("AnswerMsg")
    val Position = Value("PositionMsg")
    val Quiz = Value("QuizMsg")
    val State = Value("StateMsg")
    val Poi = Value("PoiMsg")
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

    def apply(sender: Any, entity: Serializable): Message = entity match {
        case _: Position => PositionMsgImpl(sender.toString, entity.defaultRepresentation)
        case _: Quiz => QuizMsgImpl(sender.toString, entity.defaultRepresentation)
        case _: Clue => ClueMsgImpl(sender.toString, entity.defaultRepresentation)
        case x: TreasureHunt => CreatedMsgImpl(sender.toString, treasureHuntID = x.asInstanceOf[TreasureHunt].ID)
        case _ => throw new NoMsgDefinedException(s"No message defined for $entity class")
    }

}
