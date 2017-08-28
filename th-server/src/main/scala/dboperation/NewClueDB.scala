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
    def insertNewClue(text: String, idOrganizer: Int): Unit
}

case class NewClueDBImpl() extends NewClueDB {
    /**
      * Method to insert a new clue in the DB
      *
      * @param text        text of the clue
      * @param idOrganizer organizer that have created this clue
      */
    override def insertNewClue(text: String, idOrganizer: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"INSERT INTO clue (clue_text,id_organizer) VALUES ('${text}',${idOrganizer})"
        statement.executeUpdate(query)
        connection.close()
    }
}