package utils


import java.sql.{Connection, DriverManager}

/** An Entity that establish a connection to the DB
  *
  */
trait DBConnectionManager {

    /**
      * Method to initialize connection
      *
      * @return the initialized connection variable
      */
    def establishConnection: Connection
}

case class DBConnectionManagerImpl() extends DBConnectionManager {
    /**
      * Method to initialize connection
      *
      * @return the initialized connection variable
      */
    override def establishConnection: Connection = {
        var connection: Connection = null
        try {
            Class.forName(ConnectionInfo.driver)
            connection = DriverManager.getConnection(ConnectionInfo.url, ConnectionInfo.username, ConnectionInfo.password)
        } catch {
            case e: Exception => e.printStackTrace
        }
        return connection
    }
}
