/**
  * Created by CarmineVattimo on 19/07/2017.
  */
import org.scalatest.FunSuite

class QuizTest extends FunSuite {

    val question = "what time is it?"
    val answer = "who cares"
    val emptyAnswer = ""
    val quiz = new QuizImpl(question,answer)

    test("Question must be a String") {
        assert(quiz.getQuestion.isInstanceOf[String])
    }

    test("Answer must be a String") {
        assert(quiz.getAnswer.isInstanceOf[String])
    }

    test("Question must be not empty") {
        assert(quiz.getQuestion != "")
    }

    test("Answer must be not empty") {
        assert(quiz.getAnswer != "")
    }

}
