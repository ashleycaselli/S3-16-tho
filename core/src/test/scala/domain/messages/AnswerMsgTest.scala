package domain.messages

import com.typesafe.scalalogging.Logger
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class AnswerMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Master Splinter"
    private val payload: String = ""
    private var answerMessage: AnswerMsg = null
    val logger = Logger(LoggerFactory.getLogger("test"))

    before {
        answerMessage = AnswerMsgImpl(sender)
    }

    test("AnswerMsg sender must be String") {
        assert(answerMessage.sender.isInstanceOf[String] && answerMessage.payload.isInstanceOf[String])
    }

    test("AnswerMsg content checking") {
        assert(answerMessage.sender === sender && answerMessage.payload === payload)
    }

    test("Checking AnswerMsg's serializability") {
        implicit val answerMsgReads = Json.reads[AnswerMsgImpl]
        val newAnswerMsg = Json.parse(answerMessage.defaultRepresentation).as[AnswerMsgImpl]
        logger.info(s"oldClueMsg: ${answerMessage.defaultRepresentation.toString}")
        logger.info(s"newClueMsg: ${newAnswerMsg.defaultRepresentation.toString}")
        assert(answerMessage === newAnswerMsg)
    }

}