package domain.messages

import domain._

/**
  * A Message is an entity that could be spread through a communication channel.
  */
trait Message extends Serializable {

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
