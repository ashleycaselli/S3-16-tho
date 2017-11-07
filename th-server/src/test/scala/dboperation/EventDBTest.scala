package dboperation

import java.sql.Connection

import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class EventDBTest extends FunSuite with BeforeAndAfter {

    private var eventDB: EventDB = null
    private var connectionManager: DBConnectionManager = null

    before {
        eventDB = new EventDBImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After a team started a treasure hunt, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1
        val eventType = 1

        eventDB.teamStartTreasureHunt(idTH, idTeam)

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
        val idPOI: Int = 1

        eventDB.teamReachPOI(idTH, idTeam, idPOI)

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

        eventDB.teamReceiveQuiz(idTH, idTeam, idQuiz)

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

        eventDB.teamSolveQuiz(idTH, idTeam, idQuiz)

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

        eventDB.teamReceiveClue(idTH, idTeam, idClue)

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

        eventDB.teamFinishTreasureHunt(idTH, idTeam)

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

    test("After an organizer open treasure hunt subscription, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idOrganizer: Int = 1
        val eventType = 7

        eventDB.organizerOpenTreasureHuntSubscription(idTH, idOrganizer)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_organizer = $idOrganizer")
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

    test("After an organizer start a treasure hunt, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idOrganizer: Int = 1
        val eventType = 8

        eventDB.organizerStartTreasureHunt(idTH, idOrganizer)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_organizer = $idOrganizer")
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

    test("After an organizer close a treasure hunt, it is present the log in event_log table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idOrganizer: Int = 1
        val eventType = 9

        eventDB.organizerCloseTreasureHunt(idTH, idOrganizer)

        /*---CHECK IF INSERT IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT COUNT(*) FROM event_log WHERE event_type = $eventType AND id_treasure_hunt = $idTH AND id_organizer = $idOrganizer")
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
