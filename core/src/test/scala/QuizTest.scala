import org.scalatest.FunSuite

class QuizTest extends FunSuite {

    val question = "What time is it?"
    val answer = "Who cares."
    val quiz = QuizImpl(question, answer)

    test("Question must be a String") {
        assert(quiz.question.isInstanceOf[String])
    }

    test("Answer must be a String") {
        assert(quiz.answer.isInstanceOf[String])
    }

    test("Question must be not empty") {
        assert(!quiz.question.isEmpty)
    }

    test("Answer must be not empty") {
        assert(!quiz.answer.isEmpty)
    }

}
