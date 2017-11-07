package domain

import play.api.libs.json.{JsPath, Json, Reads, Writes}

/** An Entity that contains a Question and an Answer
  *
  */
trait Quiz extends Serializable {

    /**
      * Property to get the Quiz's ID.
      *
      * @return the Quiz's ID
      */
    def ID: Int

    /**
      * Property to set the Quiz's ID.
      *
      * @param ID a numeric Identifier
      */
    def ID_=(ID: Int): Unit

    /**
      * Property to get Question.
      *
      * @return the text of the Question
      */
    def question: String

    /**
      * Property to set the Question.
      *
      * @param question a sentence that represents a question
      */
    def question_=(question: String): Unit

    /**
      * Property to get the Answer.
      *
      * @return the text of the Answer
      */
    def answer: String

    /**
      * Property to set the Answer.
      *
      * @param question a sentence that represents an answer
      */
    def answer_=(question: String): Unit

}


case class QuizImpl(private var _ID: Int = 0, override var question: String, override var answer: String) extends Quiz {

    implicit val quizWrites = new Writes[QuizImpl] {
        def writes(quiz: QuizImpl) = Json.obj(
            "ID" -> ID,
            "question" -> question,
            "answer" -> answer)
    }

    /**
      * Property to get the Quiz's ID
      *
      * @return an Int that contains the Quiz's ID
      */
    override def ID: Int = _ID

    /**
      * Property to set the Quiz's ID
      *
      * @param ID the Quiz's ID
      */
    override def ID_=(ID: Int): Unit = {
        require(ID >= 0)
        _ID = ID
    }

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Quiz {

    def apply(ID: Int, question: String, answer: String): Quiz = QuizImpl(ID, question, answer)

    import play.api.libs.functional.syntax._

    implicit val quizReads: Reads[Quiz] = (
        (JsPath \ "ID").read[Int] and
            (JsPath \ "question").read[String] and
            (JsPath \ "answer").read[String]
        ) (Quiz.apply _)

}
