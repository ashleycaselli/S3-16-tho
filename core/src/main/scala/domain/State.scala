package domain

import domain.messages.StateType.StateType
import play.api.libs.json.{JsPath, Json, Reads}

trait State extends Serializable {

    def state: StateType

    def treasureHuntID: Int

}

case class StateImpl(override val state: StateType, override val treasureHuntID: Int) extends State {

    implicit val stateWrites = Json.writes[StateImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object State {

    def apply(state: StateType, treasureHuntID: Int): StateImpl = StateImpl(state, treasureHuntID)

    import play.api.libs.functional.syntax._

    implicit val stateReads: Reads[State] = (
            (JsPath \ "state").read[StateType] and
                    (JsPath \ "treasureHuntID").read[Int]
            ) (State.apply _)

}
