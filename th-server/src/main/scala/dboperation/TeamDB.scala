package dboperation

import java.sql.Connection

import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait TeamDB {
    /**
      * Method to subscribe a Team in a TH and insert a record in the DB
      *
      * @param idTH   ID of the Treasure Hunt the team want to join
      * @param idTeam ID of the team that want to join the Treasure Hunt
      */
    def subscribeTreasureHunt(idTH: Int, idTeam: Int): Unit
}

case class TeamDBImpl() extends TeamDB {
    /**
      * Method to subscribe a Team in a TH and insert a record in the DB
      *
      * @param idTH   ID of the Treasure Hunt the team want to join
      * @param idTeam ID of the team that want to join the Treasure Hunt
      */
    def subscribeTreasureHunt(idTH: Int, idTeam: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"INSERT INTO team_in_treasure_hunt (id_treasure_hunt, id_team) VALUES (${idTH},${idTeam})"
        statement.executeUpdate(query)
        connection.close()
    }
}