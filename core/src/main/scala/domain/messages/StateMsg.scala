package domain.messages

import play.api.libs.json._
import utils.EnumUtils

/** An enumeration that represents the available states.
  *
  */
object StateType extends Enumeration {
    type StateType = Value
    val Created = Value("Created")
    val Start = Value("Start")
    val Stop = Value("Stop")

    implicit val enumReads: Reads[StateType] = EnumUtils.enumReads(StateType)

    implicit def enumWrites: Writes[StateType] = EnumUtils.enumWrites

}

/** A message that contains a Treasure Hunt's State.
  *
  */
trait StateMsg extends Message {

    def messageType = msgType.State

    implicit val stateMsgWrites = new Writes[StateMsg] {
        def writes(msg: StateMsg) = Json.obj(
            "messageType" -> messageType,
            "sender" -> sender,
            "payload" -> payload)
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

/**
  * A message that contains the Created state
  *
  * @param sender  a string that contains the sender
  * @param payload a string that contains the payload
  */
case class StateMsgImpl(override val sender: String, override val payload: String) extends StateMsg
