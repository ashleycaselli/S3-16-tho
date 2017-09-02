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
    def insertNewQuiz(question: String, answer: String, idOrganizer: Int): Int
}

case class NewQuizDBImpl() extends NewQuizDB {
    /**
      * Method to insert a new quiz in the DB
      *
      * @param question    text of the question
      * @param answer      answer of the question
      * @param idOrganizer organizer that have created this quiz
      */
    override def insertNewQuiz(question: String, answer: String, idOrganizer: Int): Int = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        var query = s"INSERT INTO quiz (text,answer,id_organizer) VALUES ('${question}','${answer}',${idOrganizer})"
        statement.executeUpdate(query)
        query = s"SELECT MAX(id_quiz) FROM quiz"
        val rs = statement.executeQuery(query)
        var idNewQuiz = 0
        while (rs.next) {
            idNewQuiz = rs.getInt(1)
        }
        connection.close()
        idNewQuiz
    }
}
