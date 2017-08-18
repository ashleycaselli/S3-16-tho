package domain.messages

import play.api.libs.json.Json

trait QuizMsg extends Message{

}

case class QuizMsgImpl(override val sender: String, override val payload: String) extends QuizMsg {

    implicit val quizMsgWrites = Json.writes[QuizMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}
