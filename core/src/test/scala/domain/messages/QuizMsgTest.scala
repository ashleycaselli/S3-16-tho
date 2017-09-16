package domain.messages

import com.typesafe.scalalogging.Logger
import domain.{Quiz, QuizImpl}
import org.scalatest.{BeforeAndAfter, FunSuite}
import play.api.libs.json.Json

class QuizMsgTest extends FunSuite with BeforeAndAfter {

    private val sender = "Riddler"
    private val quizQuestion = "Everyone has me but nobody can lose me. What am I?"
    private val quizAnswer = "Shadow"
    private var quiz: Quiz = _
    private var quizMessage: QuizMsg = _
    val logger = Logger[QuizMsgTest]

    before {
        quiz = QuizImpl(quizQuestion, quizAnswer)
        quizMessage = QuizMsgImpl(sender, quiz.defaultRepresentation)
    }

    test("QuizMsg sender and payload must be String") {
        assert(quizMessage.sender.isInstanceOf[String] && quizMessage.payload.isInstanceOf[String])
    }

    test("QuizMsg content checking") {
        assert(quizMessage.sender === sender && quizMessage.payload === quiz.defaultRepresentation)
    }

    test("Checking QuizMsg's serializability") {
        val newQuizMsg = Json.parse(quizMessage.defaultRepresentation).as[Message]
        logger info s"oldQuizMsg: ${quizMessage.defaultRepresentation.toString}"
        logger info s"newQuizMsg: ${newQuizMsg.defaultRepresentation.toString}"
        assert(quizMessage === newQuizMsg)
    }

}