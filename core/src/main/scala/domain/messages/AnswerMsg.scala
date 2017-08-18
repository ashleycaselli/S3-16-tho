package domain.messages

import play.api.libs.json.Json

trait AnswerMsg extends Message {

}

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
