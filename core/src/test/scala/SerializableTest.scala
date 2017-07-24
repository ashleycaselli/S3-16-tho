import org.scalatest.FunSuite
import play.api.libs.json.{JsValue, Json}

class SerializableTest extends FunSuite {

    val clue = new ClueImpl("fake clue")
    val quiz = new QuizImpl("fake question", "fake answer")

    test("Serialized Clue must be a String") {
        assert(clue.defaultRepresentation.isInstanceOf[String])
    }

    test("Serialized Quiz must be a Json that contains quiz question & answer") {
        val jsonRepresentation: JsValue = Json parse quiz.defaultRepresentation
        val jsonQuestion = (jsonRepresentation \ "Question").get toString
        val jsonAnswer = (jsonRepresentation \ "Answer").get toString

        assert(jsonQuestion.substring(1, jsonQuestion.length - 1) == quiz.question)
        assert(jsonAnswer.substring(1, jsonAnswer.length - 1) == quiz.answer)
    }
}
