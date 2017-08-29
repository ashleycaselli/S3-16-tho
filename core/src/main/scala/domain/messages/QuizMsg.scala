package domain.messages

import play.api.libs.json.{Json, Writes}

/** A message that contains a Quiz.
  *
  */
trait QuizMsg extends Message {
    def messageType = msgType.Quiz
}

/**
  * A message that contains a quiz to unlock the next clue in a Treasure Hunt
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class QuizMsgImpl(override val sender: String, override val payload: String) extends QuizMsg {

    implicit val quizMsgWrites = new Writes[QuizMsgImpl] {
        def writes(quizMsg: QuizMsgImpl) = Json.obj(
            "messageType" -> messageType,
            "sender" -> sender,
            "payload" -> payload)
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}
