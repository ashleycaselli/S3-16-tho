package domain.messages

import com.typesafe.scalalogging.Logger
import org.scalatest.{BeforeAndAfter, FunSuite}
import play.api.libs.json.Json

class EnrollmentMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Team leader"
    private val treasureHuntID = "63TYH399"
    private var enrollmentMessage: EnrollmentMsg = _
    val logger = Logger[EnrollmentMsgTest]

    before {
        enrollmentMessage = EnrollmentMsgImpl(sender, treasureHuntID)
    }

    test("EnrollmentMsg sender and payload must be String") {
        assert(enrollmentMessage.sender.isInstanceOf[String] && enrollmentMessage.payload.isInstanceOf[String])
    }

    test("EnrollmentMsg content checking") {
        assert(enrollmentMessage.sender === sender && enrollmentMessage.payload === treasureHuntID)
    }

    test("Checking EnrollmentMsg's serializability") {
        val newEnrollmentMsg = Json.parse(enrollmentMessage.defaultRepresentation).as[Message]
        logger info s"oldClueMsg: ${enrollmentMessage.defaultRepresentation.toString}"
        logger info s"newClueMsg: ${newEnrollmentMsg.defaultRepresentation.toString}"
        assert(enrollmentMessage === newEnrollmentMsg)
    }

}