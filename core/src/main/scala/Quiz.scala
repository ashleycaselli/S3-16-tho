import play.api.libs.json.{JsValue, Json}

/** An Entity that contains Question and Answer
  *
  */
trait Quiz extends Serializable {
    /**
      * getter of the Question
      *
      * @return the text of the Question
      */
    def getQuestion: String

    /**
      * getter of the Answer
      *
      * @return the text of the Answer
      */
    def getAnswer: String
}


class QuizImpl(question: String, answer: String) extends Quiz {
    /**
      * getter of the Question
      *
      * @return the text of the Question
      */
    override def getQuestion: String = {
        return question
    }

    /**
      * getter of the Answer
      *
      * @return the text of the Answer
      */
    override def getAnswer: String = {
        return answer
    }

    /**
      * getter of the representation
      *
      * @return a String containing the representation
      */
    override def getDefaultRepresentation: String = {
        val jsonRepresentation: JsValue = Json.obj("Question" -> question, "Answer" -> answer)
        return jsonRepresentation.toString()
    }
}
