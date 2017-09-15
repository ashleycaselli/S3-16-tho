package domain

import play.api.libs.json.{JsPath, Json, Reads}

/** An Entity that contains a Question and an Answer
  *
  */
trait Quiz extends Serializable {

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


case class QuizImpl(override var question: String, override var answer: String) extends Quiz {

    implicit val quizWrites = Json.writes[QuizImpl]

    /**
      * Property for getting an entity's String representation.
      *
      * @return a String containing the representation
      */
    override def defaultRepresentation: String = Json toJson this toString

}

object Quiz {

    def apply(question: String, answer: String): Quiz = QuizImpl(question, answer)

    import play.api.libs.functional.syntax._

    implicit val quizReads: Reads[Quiz] = (
            (JsPath \ "question").read[String] and
                    (JsPath \ "answer").read[String]
            ) (Quiz.apply _)

}
