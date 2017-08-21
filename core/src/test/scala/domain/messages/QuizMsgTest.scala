package domain.messages

import com.typesafe.scalalogging.Logger
import domain.{Quiz, QuizImpl}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class QuizMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Riddler"
    private val quizQuestion = "Everyone has me but nobody can lose me. What am I?"
    private val quizAnswer = "Shadow"
    private var quiz: Quiz = null
    private var quizMessage: QuizMsg = null
    val logger = Logger(LoggerFactory.getLogger("test"))

    before {
        quiz = QuizImpl(quizQuestion, quizAnswer)
        quizMessage =  QuizMsgImpl(sender, quiz.defaultRepresentation)
    }

    test("QuizMsg sender and payload must be String") {
        assert(quizMessage.sender.isInstanceOf[String] && quizMessage.payload.isInstanceOf[String])
    }

    test("QuizMsg content checking") {
        assert(quizMessage.sender === sender && quizMessage.payload === quiz.defaultRepresentation)
    }

    test("Checking QuizMsg's serializability") {
        implicit val quizMsgReads = Json.reads[QuizMsgImpl]
        val newQuizMsg = Json.parse(quizMessage.defaultRepresentation).as[QuizMsgImpl]
        logger.info(s"oldQuizMsg: ${quizMessage.defaultRepresentation.toString}")
        logger.info(s"newQuizMsg: ${newQuizMsg.defaultRepresentation.toString}")
        assert(quizMessage === newQuizMsg)
    }

}