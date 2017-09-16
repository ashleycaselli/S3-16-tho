package domain

import com.typesafe.scalalogging.Logger
import org.scalatest.FunSuite
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class QuizTest extends FunSuite {

    val question = "What time is it?"
    val answer = "Who cares."
    val quiz = QuizImpl(question, answer)

    test("Question must be not empty") {
        assert(quiz.question.nonEmpty)
    }

    test("Answer must be not empty") {
        assert(quiz.answer.nonEmpty)
    }

    test("Checking Quiz serializability") {
        val logger = Logger(LoggerFactory.getLogger("test"))
        val newQuiz = Json.parse(quiz.defaultRepresentation).as[Quiz]
        logger info s"oldQuiz: ${quiz.defaultRepresentation.toString}"
        logger info s"newQuiz: ${newQuiz.defaultRepresentation.toString}"
        assert(quiz === newQuiz)
    }

}
