package domain.messages

import play.api.libs.json.{Json, Reads, Writes}
import utils.EnumUtils

/** An enumeration that represents the available states.
  *
  */
object State extends Enumeration {
    type State = Value
    val Created = Value("Created")
    val Start = Value("Start")
    val Stop = Value("Stop")

    implicit val enumReads: Reads[State] = EnumUtils.enumReads(State)

    implicit def enumWrites: Writes[State] = EnumUtils.enumWrites

}

/** A message that contains a Treasure Hunt's State.
  *
  */
trait StateMsg extends Message {

    def messageType = msgType.State

    def treasureHuntID: String

    implicit val stateMsgWrites = new Writes[StateMsg] {
        def writes(msg: StateMsg) = Json.obj(
            "sender" -> sender,
            "payload" -> Json.obj(
                "th" -> treasureHuntID,
                "state" -> payload))
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation = Json toJson this toString()

}

/**
  * A message that contains the Created state
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class CreatedMsgImpl(override val sender: String, override val payload: String = State.Created.toString, override val treasureHuntID: String) extends StateMsg

/**
  * A message that contains a state (start)
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class StartMsgImpl(override val sender: String, override val payload: String = State.Start.toString, override val treasureHuntID: String) extends StateMsg

/**
  * A message that contains a state (stop)
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class StopMsgImpl(override val sender: String, override val payload: String = State.Stop.toString, override val treasureHuntID: String) extends StateMsg
