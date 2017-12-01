package dboperation

import java.sql.Connection

import org.joda.time.DateTime
import utils.{DBConnectionManager, DBConnectionManagerImpl}

trait EventDB {

    /**
      * Method to do team operations
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idPoi       identifier of the POI
      * @param idQuiz      identifier of the quiz
      * @param idClue      identifier of the clue
      * @param description event description
      * @param eventType   identifier of the event type
      */
    def teamOperation(idTH: Int, idTeam: Int, description: String, idPoi: Int, idQuiz: Int, idClue: Int, eventType: Int): Unit

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
      * Method to register that a team received a Clue  and insert a record in the DB (event type: 4)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idClue      identifier of the Clue Received
      * @param description event description (optional parameter)
      */
    def teamReceiveClue(idTH: Int, idTeam: Int, idClue: Int, description: String = "No description provided"): Unit

    /**
      * Method to register that a team finish a TH and insert a record in the DB (event type: 5)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description (optional parameter)
      * @return a string containing the winning team name
      */
    def teamFinishTreasureHunt(idTH: Int, idTeam: Int, description: String = "No description provided"): String

    /**
      * Method to do the organizer operation
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description
      * @param eventType   id of the event type
      */
    def organizerOperation(idTH: Int, idTeam: Int, description: String, eventType: Int): Unit

    /**
      * Method to open treasure hunt subscription and insert a record in the DB (event type: 6)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idOrganizer identifier of the Organizer
      * @param description event description (optional parameter)
      */
    def organizerStartTreasureHunt(idTH: Int, idOrganizer: Int, description: String = "No description provided"): Unit

    /**
      * Method to get the correct timestamp
      *
      * @return String that contains timestamp
      *
      */
    def getCorrectTimestamp(): String
}

case class EventDBImpl() extends EventDB {

    private val ID_POI_NULL = 0
    private val ID_QUIZ_NULL = 0
    private val ID_CLUE_NULL = 0

    /**
      * Method to do team operations
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idPoi       identifier of the POI
      * @param idQuiz      identifier of the quiz
      * @param idClue      identifier of the clue
      * @param description event description (optional parameter)
      */
    override def teamOperation(idTH: Int, idTeam: Int, description: String, idPoi: Int, idQuiz: Int, idClue: Int, eventType: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val idTeamValue = if (idTeam > 0) idTeam else "NULL"
        val idPoiValue = if (idPoi > 0) idPoi else "NULL"
        val idQuizValue = if (idQuiz > 0) idQuiz else "NULL"
        val idClueValue = if (idClue > 0) idClue else "NULL"
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_team, id_poi, id_quiz, id_clue, event_description) " +
            s"VALUES ('$getCorrectTimestamp',$eventType,$idTH,$idTeamValue, $idPoiValue, $idQuizValue, $idClueValue,'$description')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to begin the TH of a Team and insert a record in the DB (event type: 1)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description (optional parameter)
      */
    override def teamStartTreasureHunt(idTH: Int, idTeam: Int, description: String = "Team Start a Tresure Hunt"): Unit = {
        val eventType = 1
        teamOperation(idTH, idTeam, description, ID_POI_NULL, ID_QUIZ_NULL, ID_CLUE_NULL, eventType)
    }

    /**
      * Method to register a POI reached by a team and insert a record in the DB (event type: 2)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idPoi       identifier of the POI reached
      * @param description event description (optional parameter)
      */
    override def teamReachPOI(idTH: Int, idTeam: Int, idPoi: Int, description: String = "Team Reach a POI"): Unit = {
        val eventType = 2
        teamOperation(idTH, idTeam, description, idPoi, ID_QUIZ_NULL, ID_CLUE_NULL, eventType)
    }

    /**
      * Method to register a Quiz sent to a team and insert a record in the DB (event type: 3)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idQuiz      identifier of the sent Quiz
      * @param description event description (optional parameter)
      */
    override def teamReceiveQuiz(idTH: Int, idTeam: Int, idQuiz: Int, description: String = "Team Receive a Quiz"): Unit = {
        val eventType = 3
        teamOperation(idTH, idTeam, description, ID_POI_NULL, idQuiz, ID_CLUE_NULL, eventType)
    }

    /**
      * Method to register that a team received a Clue  and insert a record in the DB (event type: 4)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param idClue      identifier of the Clue Received
      * @param description event description (optional parameter)
      */
    override def teamReceiveClue(idTH: Int, idTeam: Int, idClue: Int, description: String = "Team Receive a Clue"): Unit = {
        val eventType = 4
        teamOperation(idTH, idTeam, description, ID_POI_NULL, ID_QUIZ_NULL, idClue, eventType)
    }

    /**
      * Method to register that a team finish a TH and insert a record in the DB (event type: 5)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idTeam      identifier of the team
      * @param description event description (optional parameter)
      * @return a string containing the winning team name
      */
    def teamFinishTreasureHunt(idTH: Int, idTeam: Int, description: String = "No description provided"): String = {
        val eventType = 5
        teamOperation(idTH, idTeam, description, ID_POI_NULL, ID_QUIZ_NULL, ID_CLUE_NULL, eventType)
        //cerco il nome del team
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"SELECT name FROM team WHERE id_team = ${idTeam}"
        val rs = statement.executeQuery(query)
        var teamName = ""
        while (rs.next()) {
            teamName = rs.getString(1)
        }
        connection.close
        teamName
    }

    /**
      * Method to do the organizer operation
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idOrganizer identifier of the organizer
      * @param description event description
      * @param eventType   id of the event type
      */
    override def organizerOperation(idTH: Int, idOrganizer: Int, description: String, eventType: Int): Unit = {
        val connectionManager: DBConnectionManager = new DBConnectionManagerImpl
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val query = s"INSERT INTO event_log (timestamp, event_type, id_treasure_hunt, id_organizer, event_description) VALUES ('$getCorrectTimestamp',$eventType,$idTH, $idOrganizer, '$description')"
        statement.executeUpdate(query)
        connection.close
    }

    /**
      * Method to open treasure hunt subscription and insert a record in the DB (event type: 6)
      *
      * @param idTH        identifier of the Treasure Hunt
      * @param idOrganizer identifier of the Organizer
      * @param description event description (optional parameter)
      */
    override def organizerStartTreasureHunt(idTH: Int, idOrganizer: Int, description: String = "Organizer Start a Tresure Hunt"): Unit = {
        val eventType = 6
        organizerOperation(idTH, idOrganizer, description, eventType)
    }

    /**
      * Method to get the correct timestamp
      *
      * @return String that contains timestamp
      *
      */
    override def getCorrectTimestamp() = {
        val timestamp = DateTime.now()
        val correctTimestamp = timestamp.toLocalDate + " " + timestamp.getHourOfDay + ":" + timestamp.getMinuteOfHour + ":" + timestamp.getSecondOfMinute
        correctTimestamp
    }


}
