/**
  * Created by CarmineVattimo on 19/07/2017.
  */
import org.scalatest.FunSuite
import play.api.libs.json.{JsObject, JsValue, Json}

class SerializableTest extends FunSuite {

    val clue = new ClueImpl("fake clue")
    val quiz = new QuizImpl("fake question","fake answer")

    test("serialized clue must be a String") {
        assert(clue.getDefaultRepresentation.isInstanceOf[String])
    }

    test("serialized quiz must be a Json that contains quiz question & answer") {

        val jsonRepresentation: JsValue = Json.parse(quiz.getDefaultRepresentation)
        val jsonQuestion = (jsonRepresentation \ "Question").get.toString()
        val jsonAnswer = (jsonRepresentation \ "Answer").get.toString()

        assert(jsonQuestion.substring(1, jsonQuestion.length-1) == quiz.getQuestion)
        assert(jsonAnswer.substring(1, jsonAnswer.length-1) == quiz.getAnswer)
    }
}
