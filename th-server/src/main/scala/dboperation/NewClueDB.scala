package dboperation

import java.sql.Connection

import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait NewClueDB {
    /**
      * Method to insert a new clue in the DB
      *
      * @param text        text of the clue
      * @param idOrganizer organizer that have created this clue
      */
    def insertNewClue(text: String, idOrganizer: Int): Int
}

case class NewClueDBImpl() extends NewClueDB {
    /**
      * Method to insert a new clue in the DB
      *
      * @param text        text of the clue
      * @param idOrganizer organizer that have created this clue
      */
    override def insertNewClue(text: String, idOrganizer: Int): Int = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        var query = s"INSERT INTO clue (clue_text,id_organizer) VALUES ('${text}',${idOrganizer})"
        statement.executeUpdate(query)
        query = s"SELECT MAX(id_clue) FROM clue"
        val rs = statement.executeQuery(query)
        var idNewClue = 0
        while (rs.next) {
            idNewClue = rs.getInt(1)
        }
        connection.close()
        idNewClue
    }
}