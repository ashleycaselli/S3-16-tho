package dboperation

import java.sql.Connection

import domain.{POIImpl, PositionImpl}
import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class POIDBTest extends FunSuite with BeforeAndAfter {
    private var POI: POIDB = new POIDBImpl
    private var connectionManager: DBConnectionManager = new DBConnectionManagerImpl
    private var connection: Connection = connectionManager.establishConnection
    private var statement = connection.createStatement()

    before {
        connection = connectionManager.establishConnection
        statement = connection.createStatement
    }

    after {
        connection.close
    }

    test("After the insert of the new POI, it is present in point_of_interest table and has the correct value") {
        val name: String = "The First POI"
        val latitude = 43.8402492
        val longitude = 13.02423991
        val position = PositionImpl(latitude, longitude)
        val idOrganizer: Int = 1
        val treasureHuntID: Int = 1
        val poi = POIImpl(position = position, name = name, treasureHuntID = treasureHuntID)

        val idNewPOI = POI.insertNewPOI(poi, idOrganizer)

        var rs = statement.executeQuery("SELECT MAX(id_poi) FROM point_of_interest")
        var idPOI = 0
        while (rs.next) {
            idPOI = rs.getInt("MAX(id_poi)")
        }

        assert(idNewPOI == idPOI)

        /*---CHECK IF INSERT IS CORRECT---*/
        rs = statement.executeQuery(s"SELECT * FROM point_of_interest WHERE id_poi = ${idPOI}")
        while (rs.next) {
            assert(rs.getString("name") == name)
            assert(rs.getDouble("latitude") == latitude)
            assert(rs.getDouble("longitude") == longitude)
            assert(rs.getInt("id_organizer") == idOrganizer)
        }

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM poi_in_treasure_hunt WHERE id_poi = ${idPOI}")
        statement.executeUpdate(s"DELETE FROM point_of_interest WHERE id_poi = ${idPOI}")
    }

    test("After setting a clue to a POI the \"id_clue\" field is populated with the corrected value") {
        val idPOI: Int = 1
        val idClue: Int = 1

        POI.setClue(idPOI, idClue)

        /*---CHECK IF UPDATE IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT id_clue FROM point_of_interest WHERE id_poi = ${idPOI}")
        var idClueSelected = 0
        while (rs.next) {
            idClueSelected = rs.getInt("id_clue")
        }
        assert(idClueSelected == idClue)
    }

    test("After setting a quiz to a POI the \"id_quiz\" field is populated with the corrected value") {
        val idPOI: Int = 1
        val idQuiz: Int = 1

        POI.setQuiz(idPOI, idQuiz)

        /*---CHECK IF UPDATE IS CORRECT---*/
        val rs = statement.executeQuery(s"SELECT id_quiz FROM point_of_interest WHERE id_poi = ${idPOI}")
        var idQuizSelected = 0
        while (rs.next) {
            idQuizSelected = rs.getInt("id_quiz")
        }
        assert(idQuizSelected == idQuiz)
    }
}
