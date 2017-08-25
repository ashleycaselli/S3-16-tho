package dboperation

import java.sql.Connection

import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class NewTreasureHuntDBTest extends FunSuite with BeforeAndAfter {
    private var newTreasureHunt: NewTreasureHuntDB = null
    private var connectionManager: DBConnectionManager = null

    before {
        newTreasureHunt = new NewTreasureHuntDBImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After the new treasure hunt insert, it is present in treasure_hunt table and has the correct value") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val name: String = "TestInsertingTH"
        val location: String = "Gotham City"
        val startDate: DateTime = DateTime.now()
        val startTime: String = "20:30"
        val idOrganizer: Int = 1

        new NewTreasureHuntDBImpl().insertNewTreasureHunt(name, location, startDate, startTime, idOrganizer)

        var rs = statement.executeQuery("SELECT MAX(id_treasure_hunt) FROM treasure_hunt")
        var idTH = 0
        while (rs.next) {
            idTH = rs.getInt("MAX(id_treasure_hunt)")
        }

        /*---CHECK IF INSERT IS CORRECT---*/
        rs = statement.executeQuery(s"SELECT * FROM treasure_hunt WHERE id_treasure_hunt = ${idTH}")
        while (rs.next) {
            assert(rs.getString("name") == name)
            assert(rs.getString("location") == location)
            assert(rs.getDate("start_date").toString == startDate.toLocalDate.toString)
            assert(rs.getString("start_time") == startTime)
            assert(rs.getInt("id_organizer") == idOrganizer)
        }

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM treasure_hunt WHERE id_treasure_hunt = ${idTH}")

        connection.close()
    }
}
