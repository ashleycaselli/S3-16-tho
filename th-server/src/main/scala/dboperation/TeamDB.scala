package dboperation

import java.sql.Connection

import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait TeamDB {
    /**
      * Method to subscribe a Team in a TH and insert a record in the DB
      *
      * @param idTH     ID of the Treasure Hunt the team want to join
      * @param teamName name of the team that want to join the Treasure Hunt
      * @return ID of the team
      */
    def subscribeTreasureHunt(idTH: Int, teamName: String): Int

    /**
      * Method to unsubscribe a Team in a TH and delete a record in the DB
      *
      * @param idTH   ID of the Treasure Hunt the team want to unsubscribe
      * @param idTeam ID of the team that want to unsubscribe the Treasure Hunt
      */
    def unsubscribeTreasureHunt(idTH: Int, idTeam: Int): Unit

    /**
      * Method to check if the chosen team name is available or not
      *
      * @param teamName name of the team you want to check availability of
      */
    def checkTeamNameAvailability(teamName: String): Boolean

    /**
      * Method to create a new Team and insert a record in the DB
      *
      * @param teamName name of the team that user insert in the create new team form
      * @param password password for the team that user insert in the create new team form
      */
    def createNewTeam(teamName: String, password: String): Unit

    /**
      * Method to login a Team in the mobile app, checking team name and password
      *
      * @param teamName name of the team that user insert in the login form
      * @param password password for the team that user insert in the login form
      */
    def login(teamName: String, password: String): Boolean
}

case class TeamDBImpl() extends TeamDB {
    /**
      * Method to subscribe a Team in a TH and insert a record in the DB
      *
      * @param idTH     ID of the Treasure Hunt the team want to join
      * @param teamName name of the team that want to join the Treasure Hunt
      * @return ID of the team
      */
    def subscribeTreasureHunt(idTH: Int, teamName: String): Int = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        //cerco l'id del team
        var query = s"SELECT id_team FROM team WHERE name = '${teamName}'"
        val rs = statement.executeQuery(query)
        var idTeam = 0
        while (rs.next) {
            idTeam = rs.getInt(1)
        }
        query = s"INSERT IGNORE INTO team_in_treasure_hunt (id_treasure_hunt, id_team) VALUES (${idTH},${idTeam})"
        statement.executeUpdate(query)
        connection.close()
        idTeam
    }

    /**
      * Method to unsubscribe a Team in a TH and delete a record in the DB
      *
      * @param idTH   ID of the Treasure Hunt the team want to unsubscribe
      * @param idTeam ID of the team that want to unsubscribe the Treasure Hunt
      */
    override def unsubscribeTreasureHunt(idTH: Int, idTeam: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        var query = s"DELETE FROM team_in_treasure_hunt WHERE id_treasure_hunt = ${idTH} AND id_team = ${idTeam}"
        statement.executeUpdate(query)
        connection.close()
    }

    /**
      * Method to check if the chosen team name is available or not
      *
      * @param teamName name of the team you want to check availability of
      */
    override def checkTeamNameAvailability(teamName: String): Boolean = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"SELECT COUNT(*) FROM team WHERE name = '${teamName.toLowerCase}'"
        val rs = statement.executeQuery(query)
        var available = false
        while (rs.next) {
            if (rs.getInt(1) == 0) {
                available = true
            }
        }
        connection.close
        available
    }

    /**
      * Method to create a new Team and insert a record in the DB
      *
      * @param teamName name of the team that user insert in the create new team form
      * @param password password for the team that user insert in the create new team form
      */
    override def createNewTeam(teamName: String, password: String): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"INSERT INTO team (name, password) VALUES ('${teamName.toLowerCase}','${password}')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to login a Team in the mobile app, checking team name and password
      *
      * @param teamName name of the team that user insert in the login form
      * @param password password for the team that user insert in the login form
      */
    override def login(teamName: String, password: String): Boolean = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"SELECT COUNT(*) FROM team WHERE name = '${teamName.toLowerCase}' AND password = '${password}'"
        val rs = statement.executeQuery(query)
        var loginResult = false
        while (rs.next) {
            if (rs.getInt(1) == 1) {
                loginResult = true
            }
        }
        connection.close
        loginResult
    }
}