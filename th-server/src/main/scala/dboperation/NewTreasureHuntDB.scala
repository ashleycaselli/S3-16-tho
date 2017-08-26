package dboperation

import java.sql.Connection

import org.joda.time.DateTime
import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait NewTreasureHuntDB {
    /**
      * Method to insert a new treasure hunt in the DB
      *
      * @param name        name of treasure hunt
      * @param location    place where treasure hunt will be played
      * @param startDate   date of start
      * @param startTime   time of start
      * @param idOrganizer identifier of the organizer of the treasure hunt
      */
    def insertNewTreasureHunt(name: String, location: String, startDate: DateTime, startTime: String, idOrganizer: Int): Unit
}

case class NewTreasureHuntDBImpl() extends NewTreasureHuntDB {
    /**
      * Method to insert a new treasure hunt in the DB
      *
      * @param name        name of treasure hunt
      * @param location    place where treasure hunt will be played
      * @param startDate   date of start
      * @param startTime   time of start
      * @param idOrganizer identifier of the organizer of the treasure hunt
      */
    override def insertNewTreasureHunt(name: String, location: String, startDate: DateTime, startTime: String, idOrganizer: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"INSERT INTO treasure_hunt (name, location, start_date, start_time, id_organizer) VALUES ('${name}','${location}','${startDate.toLocalDate}','${startTime}', ${idOrganizer})"
        statement.executeUpdate(query)
        connection.close()
    }
}
