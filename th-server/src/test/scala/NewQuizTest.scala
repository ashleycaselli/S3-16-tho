import java.sql.Connection

import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class NewQuizTest extends FunSuite with BeforeAndAfter {
    private var newQuiz: NewQuiz = null
    private var connectionManager: DBConnectionManager = null

    before {
        newQuiz = new NewQuizImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After the insert of the new quiz, it is present in quiz table and has the correct value") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val question: String = "Where can you find a Bezoar?"
        val answer: String = "in the stomach of a goat"
        val idOrganizer: Int = 1

        new NewQuizImpl().insertNewQuiz(question, answer, idOrganizer)

        var rs = statement.executeQuery("SELECT MAX(id_quiz) FROM quiz")
        var idQuiz = 0
        while (rs.next) {
            idQuiz = rs.getInt("MAX(id_quiz)")
        }

        /*---CHECK IF INSERT IS CORRECT---*/
        rs = statement.executeQuery(s"SELECT * FROM quiz WHERE id_quiz = ${idQuiz}")
        while (rs.next) {
            assert(rs.getString("text") == question)
            assert(rs.getString("answer") == answer)
            assert(rs.getInt("id_organizer") == idOrganizer)
        }

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM quiz WHERE id_quiz = ${idQuiz}")

        connection.close()
    }
}
