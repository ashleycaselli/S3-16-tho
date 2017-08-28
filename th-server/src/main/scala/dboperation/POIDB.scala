package dboperation

import java.sql.Connection

import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait POIDB {
    /**
      * Method to insert a new POI in the DB
      *
      * @param name        name of the POI
      * @param latitude    Latitude of the POI
      * @param longitude   Longitude of the POI
      * @param idOrganizer organizer that have created this quiz
      */
    def insertNewPOI(name: String, latitude: Double, longitude: Double, idOrganizer: Int): Unit

    /**
      * Method to assign a Clue to a POI in the DB
      *
      * @param idPOI  Identifier of the POI
      * @param idClue Identifier of the Clue
      */
    def setClue(idPOI: Int, idClue: Int)

    /**
      * Method to assign a Quiz to a POI in the DB
      *
      * @param idPOI  Identifier of the POI
      * @param idQuiz Identifier of the Quiz
      */
    def setQuiz(idPOI: Int, idQuiz: Int)


}

case class POIDBImpl() extends POIDB {
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

    /**
      * Method to assign a Clue to a POI in the DB
      *
      * @param idPOI  Identifier of the POI
      * @param idClue Identifier of the Clue
      */
    override def setClue(idPOI: Int, idClue: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"UPDATE point_of_interest SET id_clue = ${idClue} WHERE id_poi = ${idPOI}"
        statement.executeUpdate(query)
        connection.close()
    }

    /**
      * Method to assign a Quiz to a POI in the DB
      *
      * @param idPOI  Identifier of the POI
      * @param idQuiz Identifier of the Quiz
      */
    override def setQuiz(idPOI: Int, idQuiz: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"UPDATE point_of_interest SET id_quiz = ${idQuiz} WHERE id_poi = ${idPOI}"
        statement.executeUpdate(query)
        connection.close()
    }
}