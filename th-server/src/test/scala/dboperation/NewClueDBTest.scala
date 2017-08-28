package dboperation

import java.sql.Connection

import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class NewClueDBTest extends FunSuite with BeforeAndAfter {
    private var newClue: NewClueDB = null
    private var connectionManager: DBConnectionManager = null

    before {
        newClue = new NewClueDBImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After the insert of the new clue, it is present in clue table and has the correct value") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val text: String = "Se qualcuno vuole trovare qualcosa, deve seguire i ragni!"
        val idOrganizer: Int = 1

        newClue.insertNewClue(text, idOrganizer)

        var rs = statement.executeQuery("SELECT MAX(id_clue) FROM clue")
        var idClue = 0
        while (rs.next) {
            idClue = rs.getInt("MAX(id_clue)")
        }

        /*---CHECK IF INSERT IS CORRECT---*/
        rs = statement.executeQuery(s"SELECT * FROM clue WHERE id_clue = ${idClue}")
        while (rs.next) {
            assert(rs.getString("clue_text") == text)
            assert(rs.getInt("id_organizer") == idOrganizer)
        }

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM clue WHERE id_clue= ${idClue}")

        connection.close()
    }
}
