package dboperation

import java.sql.Connection

import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class TreasureHuntEventDBTest extends FunSuite with BeforeAndAfter {

    private var treasureHuntEventDB: TreasureHuntEventDB = null
    private var connectionManager: DBConnectionManager = null

    before {
        treasureHuntEventDB = new TreasureHuntEventDBImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After a team started a treasure hunt, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1
        val eventType = 1

        treasureHuntEventDB.teamStartTreasureHunt(idTH, idTeam)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_team = $idTeam")
        var correctLog = 0
        while (rs.next) {
            correctLog = rs.getInt(1)
        }

        assert(correctLog == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM event_log WHERE id_log IN (SELECT * FROM (SELECT MAX(id_log) FROM event_log) as lastID)")

        connection.close()
    }

    test("After a team reached a POI, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1
        val eventType = 2
        val idPOI: Int = 6

        treasureHuntEventDB.teamReachPOI(idTH, idTeam, idPOI)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_team = $idTeam AND id_poi = $idPOI")
        var correctLog = 0
        while (rs.next) {
            correctLog = rs.getInt(1)
        }

        assert(correctLog == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM event_log WHERE id_log IN (SELECT * FROM (SELECT MAX(id_log) FROM event_log) as lastID)")

        connection.close()
    }

    test("After a team receive a quiz, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1
        val eventType = 3
        val idQuiz: Int = 1

        treasureHuntEventDB.teamReceiveQuiz(idTH, idTeam, idQuiz)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_team = $idTeam AND id_quiz = $idQuiz")
        var correctLog = 0
        while (rs.next) {
            correctLog = rs.getInt(1)
        }

        assert(correctLog == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM event_log WHERE id_log IN (SELECT * FROM (SELECT MAX(id_log) FROM event_log) as lastID)")

        connection.close()
    }

    test("After a team solve a Quiz, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1
        val eventType = 4
        val idQuiz: Int = 1

        treasureHuntEventDB.teamSolveQuiz(idTH, idTeam, idQuiz)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_team = $idTeam AND id_quiz = $idQuiz")
        var correctLog = 0
        while (rs.next) {
            correctLog = rs.getInt(1)
        }

        assert(correctLog == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM event_log WHERE id_log IN (SELECT * FROM (SELECT MAX(id_log) FROM event_log) as lastID)")

        connection.close()
    }

    test("After a team receive a clue, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1
        val eventType = 5
        val idClue: Int = 1

        treasureHuntEventDB.teamReceiveClue(idTH, idTeam, idClue)

        /*---CHECK IF INSERT IS CORRECT---*/
        var rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_team = $idTeam AND id_clue = $idClue")
        var correctLog = 0
        while (rs.next) {
            correctLog = rs.getInt(1)
        }

        assert(correctLog == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM event_log WHERE id_log IN (SELECT * FROM (SELECT MAX(id_log) FROM event_log) as lastID)")

        connection.close()
    }

    test("After a team finish a treasure hunt, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1
        val eventType = 6

        treasureHuntEventDB.teamFinishTreasureHunt(idTH, idTeam)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_team = $idTeam")
        var correctLog = 0
        while (rs.next) {
            correctLog = rs.getInt(1)
        }

        assert(correctLog == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM event_log WHERE id_log IN (SELECT * FROM (SELECT MAX(id_log) FROM event_log) as lastID)")

        connection.close()
    }

}
