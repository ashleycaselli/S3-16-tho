package domain.messages

import play.api.libs.json.Json

object State extends Enumeration {
    type State = Value
    val Start = Value("Start")
    val Stop = Value("Stop")
}

trait StateMsg extends Message {

}

case class StartMsgImpl(override val sender: String, override val payload: String = State.Start.toString) extends StateMsg {

    implicit val startMsgWrites = Json.writes[StartMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}

case class StopMsgImpl(override val sender: String, override val payload: String = State.Stop.toString) extends StateMsg {

    implicit val stopMsgWrites = Json.writes[StopMsgImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = {
        Json toJson this toString
    }
}