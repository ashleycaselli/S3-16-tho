package dboperation

import java.sql.Connection

import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait NewQuizDB {
    /**
      * Method to insert a new quiz in the DB
      *
      * @param question    text of the question
      * @param answer      answer of the question
      * @param idOrganizer organizer that have created this quiz
      */
    def insertNewQuiz(question: String, answer: String, idOrganizer: Int): Unit
}

case class NewQuizDBImpl() extends NewQuizDB {
    /**
      * Method to insert a new quiz in the DB
      *
      * @param question text of the question
      * @param answer   answer of the question
      */
    override def insertNewQuiz(question: String, answer: String, idOrganizer: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"INSERT INTO quiz (text,answer,id_organizer) VALUES ('${question}','${answer}',${idOrganizer})"
        statement.executeUpdate(query)
        connection.close()
    }
}
