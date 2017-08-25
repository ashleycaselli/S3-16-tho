package dboperation

import java.sql.Connection

import domain.TeamImpl
import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class TeamDBTest extends FunSuite with BeforeAndAfter {
    private var team: TeamDB = null
    private var connectionManager: DBConnectionManager = null

    before {
        team = new TeamDBImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After the subscribtion of a team in a treasure hunt, it is present in team_in_treasure_hunt table and has the correct value") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1

        new TeamImpl().subscribeTreasureHunt(idTH, idTeam)

        /*---CHECK IF INSERT IS CORRECT---*/
        var rs = statement.executeQuery(s"SELECT COUNT(*) FROM team_in_treasure_hunt WHERE id_treasure_hunt = ${idTH} AND id_team = ${idTeam}")
        var subscribtion = 0
        while (rs.next) {
            subscribtion = rs.getInt("COUNT(*)")
        }

        assert(subscribtion == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM team_in_treasure_hunt WHERE id_treasure_hunt = ${idTH} AND id_team = ${idTeam}")

        connection.close()
    }
}
