package dboperation

import java.sql.Connection

import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait NewPOIDB {
    /**
      * Method to insert a new POI in the DB
      *
      * @param name        name of the POI
      * @param latitude    Latitude of the POI
      * @param longitude   Longitude of the POI
      * @param idOrganizer organizer that have created this quiz
      */
    def insertNewPOI(name: String, latitude: Double, longitude: Double, idOrganizer: Int): Unit
}

case class NewPOIDBImpl() extends NewPOIDB {
    /**
      * Method to insert a new POI in the DB
      *
      * @param name        name of the POI
      * @param latitude    Latitude of the POI
      * @param longitude   Longitude of the POI
      * @param idOrganizer organizer that have created this quiz
      */
    override def insertNewPOI(name: String, latitude: Double, longitude: Double, idOrganizer: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"INSERT INTO point_of_interest (name,latitude,longitude,id_organizer) VALUES ('${name}',${latitude},${longitude},${idOrganizer})"
        statement.executeUpdate(query)
        connection.close()
    }
}