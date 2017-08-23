package domain.messages

import com.typesafe.scalalogging.Logger
import domain.messages.State.State
import org.scalatest.FunSuite
import play.api.libs.json.Json

class CreatedStateMsgTest extends FunSuite {

    private val sender: String = "TH-organizer"
    private val th = "TH-0"
    private val msg: StateMsg = CreatedMsgImpl(sender, treasureHuntID = th)
    private val logger = Logger[CreatedStateMsgTest]

    test("Checking sender's value") {
        assert(msg.sender === sender)
    }

    test("Checking treasure hunts's id value") {
        assert(msg.treasureHuntID === th)
    }

    test("Checking CreatedStateMsg's serializability ") {
        val rep = msg.defaultRepresentation
        logger.info(rep)
        val json = Json.parse(rep)
        assert((json \ "sender").as[String] === sender)
        assert(((json \ "payload") \ "th").as[String] === th)
        assert(((json \ "payload") \ "state").as[State] === State.Created)
    }

}
