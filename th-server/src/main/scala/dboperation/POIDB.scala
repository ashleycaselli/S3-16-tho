package dboperation

import java.sql.Connection

import domain._
import utils.{DBConnectionManager, DBConnectionManagerImpl}

import scala.collection.mutable.ListBuffer

trait POIDB {
    /**
      * Method to insert a new POI in the DB
      *
      * @param poi         the POI that we have to insert in the DB
      * @param idOrganizer organizer that have created this quiz
      */
    def insertNewPOI(poi: POI, idOrganizer: Int): Int

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

    /**
      * Method to get a list of POIs of a treasure hunt (with their quiz and clue)
      *
      * @param idTreasureHunt identifier of the selected Treasure Hunt
      * @return a list of POIs
      */
    def getPOIsList(idTreasureHunt: Int): ListBuffer[POI]

    def getFirstPoi(idTreasureHunt: Int): POI

    def getSubsequentPoi(poi: POI): POI

    /**
      * Method to remove a POI from DB (and its quiz anc clue)
      *
      * @param poi the poi to delete
      */
    def deletePoi(poi: POI): Unit
}

case class POIDBImpl() extends POIDB {
    /**
      * Method to insert a new POI in the DB
      *
      * @param poi         the POI that we have to insert in the DB
      * @param idOrganizer organizer that have created this POI
      */
    override def insertNewPOI(poi: POI, idOrganizer: Int): Int = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        var query = s"INSERT INTO point_of_interest (name,latitude,longitude,id_organizer) VALUES ('${poi.name}',${poi.position.latitude},${poi.position.longitude},${idOrganizer})"
        statement.executeUpdate(query)
        query = s"SELECT MAX(id_poi) FROM point_of_interest"
        val rs = statement.executeQuery(query)
        var idNewPOI = 0
        while (rs.next) {
            idNewPOI = rs.getInt(1)
        }
        query = s"INSERT INTO poi_in_treasure_hunt (id_poi, id_treasure_hunt) VALUES (${idNewPOI},${poi.treasureHuntID})"
        statement.executeUpdate(query)
        connection.close()
        idNewPOI
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

    /**
      * Method to get a list of POIs of a treasure hunt (with their quiz and clue)
      *
      * @param idTreasureHunt identifier of the selected Treasure Hunt
      * @return a list of POIs
      */
    override def getPOIsList(idTreasureHunt: Int) = {
        var poisList: ListBuffer[POI] = ListBuffer.empty
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"SELECT " +
            s"p.id_poi AS idPoi, " +
            s"p.name AS poiName, " +
            s"p.latitude AS latitude, " +
            s"p.longitude AS longitude, " +
            s"p.id_quiz AS idQuiz, " +
            s"q.question AS question, " +
            s"q.answer AS answer, " +
            s"p.id_clue AS idClue, " +
            s"c.clue_text AS clueText " +
            s"FROM " +
            s"point_of_interest p, " +
            s"poi_in_treasure_hunt pth, " +
            s"clue c, " +
            s"quiz q " +
            s"WHERE pth.id_poi = p.id_poi " +
            s"AND pth.id_treasure_hunt = ${idTreasureHunt} " +
            s"AND p.id_quiz = q.id_quiz " +
            s"AND p.id_clue = c.id_clue;"
        val rs = statement.executeQuery(query)
        while (rs.next) {
            val idPoi = rs.getInt("idPoi")
            val position = PositionImpl(rs.getDouble("latitude"), rs.getDouble("longitude"))
            val name = rs.getString("poiName")
            val quiz = QuizImpl(rs.getInt("idQuiz"), rs.getString("question"), rs.getString("answer"))
            val clue = ClueImpl(rs.getInt("idClue"), rs.getString("clueText"))
            poisList += POIImpl(idPoi, position, name, idTreasureHunt, quiz, clue)
        }
        connection.close()
        poisList
    }

    override def getFirstPoi(idTreasureHunt: Int): POI = {
        println("getFirstPoi")
        getPOIsList(idTreasureHunt) head
    }

    override def getSubsequentPoi(poi: POI): POI = {
        println("getsubPoi")
        val list = getPOIsList(poi.treasureHuntID)
        if (list.indexOf(poi) == list.length - 1)
            return null
        list(list.indexOf(poi) + 1)
    }

    /**
      * Method to remove a POI from DB (and its quiz anc clue)
      *
      * @param poi the poi to delete
      */
    override def deletePoi(poi: POI): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val idPoi: Int = poi.ID
        val idQuiz: Int = poi.quiz.ID
        val idClue: Int = poi.clue.ID
        val idTH: Int = poi.treasureHuntID
        var query = s"DELETE FROM poi_in_treasure_hunt WHERE id_poi = ${idPoi} AND id_treasure_hunt = ${idTH};"
        statement.executeUpdate(query)
        query = s"DELETE FROM point_of_interest WHERE id_poi = ${idPoi};"
        statement.executeUpdate(query)
        query = s"DELETE FROM quiz WHERE id_quiz = ${idQuiz};"
        statement.executeUpdate(query)
        query = s"DELETE FROM clue WHERE id_clue = ${idClue};"
        statement.executeUpdate(query)
        connection.close()
    }
}