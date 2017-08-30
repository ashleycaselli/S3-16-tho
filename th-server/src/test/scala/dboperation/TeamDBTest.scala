package dboperation

import java.sql.Connection

import org.scalatest.{BeforeAndAfter, FunSuite}
import utils.{DBConnectionManager, DBConnectionManagerImpl}

class TeamDBTest extends FunSuite with BeforeAndAfter {
    private var teamDB: TeamDB = null
    private var connectionManager: DBConnectionManager = null

    before {
        teamDB = new TeamDBImpl
        connectionManager = new DBConnectionManagerImpl
    }

    test("After the subscription of a team in a treasure hunt, it is present in team_in_treasure_hunt table and has the correct value") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1

        teamDB.subscribeTreasureHunt(idTH, idTeam)

        /*---CHECK IF INSERT IS CORRECT---*/
        var rs = statement.executeQuery(s"SELECT COUNT(*) FROM team_in_treasure_hunt WHERE id_treasure_hunt = ${idTH} AND id_team = ${idTeam}")
        var subscribtion = 0
        while (rs.next) {
            subscribtion = rs.getInt(1)
        }

        assert(subscribtion == 1)

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM team_in_treasure_hunt WHERE id_treasure_hunt = ${idTH} AND id_team = ${idTeam}")

        connection.close()
    }

    test("After an unsubscription of a team in a treasure hunt, it is not present in team_in_treasure_hunt table") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val idTH: Int = 1
        val idTeam: Int = 1

        //first of all subscribe a team in order to unsubscribe that team
        teamDB.subscribeTreasureHunt(idTH, idTeam)

        teamDB.unsubscribeTreasureHunt(idTH, idTeam)

        /*---CHECK IF DELETE IS CORRECT---*/
        var rs = statement.executeQuery(s"SELECT COUNT(*) FROM team_in_treasure_hunt WHERE id_treasure_hunt = ${idTH} AND id_team = ${idTeam}")
        var subscribtion = 0
        while (rs.next) {
            subscribtion = rs.getInt(1)
        }

        assert(subscribtion == 0)

        connection.close()
    }

    test("If a team name is not available, checkTeamNameAvailability must return false, otherwise true") {
        val availableName: String = "available name"
        val notAvailableName: String = "team ammut"

        /*---CHECK IF INSERT IT IS AVAILABLE---*/
        var availability = teamDB.checkTeamNameAvailability(availableName)
        assert(availability)

        /*---CHECK IF INSERT IT IS AVAILABLE---*/
        availability = teamDB.checkTeamNameAvailability(notAvailableName)
        assert(!availability)

    }

    test("After creating a new team, it is present in team table with the correct information") {
        val connection: Connection = connectionManager.establishConnection
        var statement = connection.createStatement

        val teamName: String = "new team"
        val password: String = "che campioni holly e benji"

        teamDB.createNewTeam(teamName, password);

        /*---CHECK IF INSERT IS CORRECT---*/
        var rs = statement.executeQuery(s"SELECT COUNT(*) FROM team WHERE name = '${teamName.toLowerCase}' AND password = '${password}'")
        while (rs.next) {
            assert(rs.getInt(1) == 1)
        }

        /*---DELETE THE LAST ADDED ROW---*/
        statement = connection.createStatement
        statement.executeUpdate(s"DELETE FROM team WHERE name = '${teamName}'")

        connection.close()
    }

    test("if the login credentials are correct, login must return true, otherwise false") {
        val correctTeamName: String = "team ammut"
        val correctPassword: String = "password"

        val wrongTeamName: String = "wrong team"
        val wrongPassword: String = "wrong password"

        assert(teamDB.login(correctTeamName, correctPassword))
        assert(!teamDB.login(correctTeamName, wrongPassword))
        assert(!teamDB.login(wrongTeamName, correctPassword))
        assert(!teamDB.login(wrongTeamName, wrongPassword))
    }
}
