package dboperation

import java.sql.Connection

import org.joda.time.DateTime
import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait TreasureHuntEventDB {

    /**
      * Method to begin the TH of a Team and insert a record in the DB (event type: 1)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description (optional parameter)
      */
    def teamStartTreasureHunt(idTH: Int, idTeam: Int, description: String = "No description provided"): Unit

    /**
      * Method to register a POI reached by a team and insert a record in the DB (event type: 2)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idPoi       identifier of the POI reached
      * @param description event description (optional parameter)
      */
    def teamReachPOI(idTH: Int, idTeam: Int, idPoi: Int, description: String = "No description provided"): Unit

    /**
      * Method to register a Quiz sent to a team and insert a record in the DB (event type: 3)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idQuiz      identifier of the sent Quiz
      * @param description event description (optional parameter)
      */
    def teamReceiveQuiz(idTH: Int, idTeam: Int, idQuiz: Int, description: String = "No description provided"): Unit

    /**
      * Method to register that a team resolved a Quiz  and insert a record in the DB (event type: 4)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idQuiz      identifier of the Quiz solved
      * @param description event description (optional parameter)
      */
    def teamSolveQuiz(idTH: Int, idTeam: Int, idQuiz: Int, description: String = "No description provided"): Unit

    /**
      * Method to register that a team received a Clue  and insert a record in the DB (event type: 5)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idClue      identifier of the Clue Received
      * @param description event description (optional parameter)
      */
    def teamReceiveClue(idTH: Int, idTeam: Int, idClue: Int, description: String = "No description provided"): Unit

    /**
      * Method to register that a team finish a TH and insert a record in the DB (event type: 6)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description (optional parameter)
      */
    def teamFinishTreasureHunt(idTH: Int, idTeam: Int, description: String = "No description provided"): Unit

}

case class TreasureHuntEventDBImpl() extends TreasureHuntEventDB {
    /**
      * Method to begin the TH of a Team and insert a record in the DB (event type: 1)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description (optional parameter)
      */
    override def teamStartTreasureHunt(idTH: Int, idTeam: Int, description: String): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val timestamp = DateTime.now()
        val correctTimestamp = timestamp.toLocalDate + " " + timestamp.getHourOfDay + ":" + timestamp.getMinuteOfHour + ":" + timestamp.getSecondOfMinute
        val eventType = 1
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_team, event_description) VALUES ('$correctTimestamp',$eventType,$idTH,$idTeam,'$description')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to register a POI reached by a team and insert a record in the DB (event type: 2)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idPoi       identifier of the POI reached
      * @param description event description (optional parameter)
      */
    override def teamReachPOI(idTH: Int, idTeam: Int, idPoi: Int, description: String): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val timestamp = DateTime.now()
        val correctTimestamp = timestamp.toLocalDate + " " + timestamp.getHourOfDay + ":" + timestamp.getMinuteOfHour + ":" + timestamp.getSecondOfMinute
        val eventType = 2
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_team, id_poi, event_description) VALUES ('$correctTimestamp',$eventType,$idTH,$idTeam,$idPoi,'$description')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to register a Quiz sent to a team and insert a record in the DB (event type: 3)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idQuiz      identifier of the sent Quiz
      * @param description event description (optional parameter)
      */
    override def teamReceiveQuiz(idTH: Int, idTeam: Int, idQuiz: Int, description: String): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val timestamp = DateTime.now()
        val correctTimestamp = timestamp.toLocalDate + " " + timestamp.getHourOfDay + ":" + timestamp.getMinuteOfHour + ":" + timestamp.getSecondOfMinute
        val eventType = 3
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_team, id_quiz, event_description) VALUES ('$correctTimestamp',$eventType,$idTH,$idTeam,$idQuiz,'$description')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to register that a team resolved a Quiz  and insert a record in the DB (event type: 4)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idQuiz      identifier of the Quiz solved
      * @param description event description (optional parameter)
      */
    override def teamSolveQuiz(idTH: Int, idTeam: Int, idQuiz: Int, description: String): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val timestamp = DateTime.now()
        val correctTimestamp = timestamp.toLocalDate + " " + timestamp.getHourOfDay + ":" + timestamp.getMinuteOfHour + ":" + timestamp.getSecondOfMinute
        val eventType = 4
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_team, id_quiz, event_description) VALUES ('$correctTimestamp',$eventType,$idTH,$idTeam,$idQuiz,'$description')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to register that a team received a Clue  and insert a record in the DB (event type: 5)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idClue      identifier of the Clue Received
      * @param description event description (optional parameter)
      */
    override def teamReceiveClue(idTH: Int, idTeam: Int, idClue: Int, description: String): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val timestamp = DateTime.now()
        val correctTimestamp = timestamp.toLocalDate + " " + timestamp.getHourOfDay + ":" + timestamp.getMinuteOfHour + ":" + timestamp.getSecondOfMinute
        val eventType = 5
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_team, id_clue, event_description) VALUES ('$correctTimestamp',$eventType,$idTH,$idTeam,$idClue,'$description')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to register that a team finish a TH and insert a record in the DB (event type: 6)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description (optional parameter)
      */
    override def teamFinishTreasureHunt(idTH: Int, idTeam: Int, description: String): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val timestamp = DateTime.now()
        val correctTimestamp = timestamp.toLocalDate + " " + timestamp.getHourOfDay + ":" + timestamp.getMinuteOfHour + ":" + timestamp.getSecondOfMinute
        val eventType = 6
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_team, event_description) VALUES ('$correctTimestamp',$eventType,$idTH,$idTeam,'$description')"
        statement.executeUpdate(query)
        connection.close
    }
}
