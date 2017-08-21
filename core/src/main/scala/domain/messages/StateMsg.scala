package domain.messages

import play.api.libs.json.Json

/** An enumeration that represents the available states.
  *
  */
object State extends Enumeration {
    type State = Value
    val Start = Value("Start")
    val Stop = Value("Stop")
}

/** A message that notify a State.
  *
  */
trait StateMsg extends Message {

}

/**
  * A message that contains a state (start)
  *
  * @param sender a string that contains the sender
  * @param payload a string that contains the payload
  */
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

/**
  * A message that contains a state (stop)
  *
  * @param sender a string that contains the sender
  * @param payload a string that contains the payload
  */
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