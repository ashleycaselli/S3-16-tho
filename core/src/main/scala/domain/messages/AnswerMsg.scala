package domain.messages

import play.api.libs.json.Json

/** An Entity that contains an Answer.
  *
  */
trait AnswerMsg extends Message {

}

/**
  * A message that represent the quiz-solved event
  *
  * @param sender a string that contains the sender
  * @param payload a string that contains the payload
  */
case class AnswerMsgImpl(override val sender: String, override val payload: String = "") extends AnswerMsg {

    implicit val answerMsgWrites = Json.writes[AnswerMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}
