import java.sql.Connection

import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class NewPOITest extends FunSuite with BeforeAndAfter {
    private var newPOI: NewPOI = null
    private var connectionManager: DBConnectionManager = null

    before {
        newPOI = new NewPOIImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After the insert of the new POI, it is present in point_of_interest table and has the correct value") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val name: String = "The First POI"
        val latitude: Double = 43.8402492
        val longitude: Double = 13.0242399
        val idOrganizer: Int = 1

        new NewPOIImpl().insertNewPOI(name, latitude, longitude, idOrganizer)

        var rs = statement.executeQuery("SELECT MAX(id_poi) FROM point_of_interest")
        var idPOI = 0
        while (rs.next) {
            idPOI = rs.getInt("MAX(id_poi)")
        }

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
        statement.executeUpdate(s"DELETE FROM point_of_interest WHERE id_poi = ${idPOI}")

        connection.close()
    }
}
