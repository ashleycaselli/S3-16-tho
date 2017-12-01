package utils

import java.sql.Connection

import org.scalatest.{BeforeAndAfter, FunSuite}

class DBConnectionManagerTest extends FunSuite with BeforeAndAfter {

    private var connectionManager: DBConnectionManager = null

    before {
        connectionManager = new DBConnectionManagerImpl
    }

    test("Connection is correctly open") {
        val connection: Connection = connectionManager.establishConnection
        val statement = connection.createStatement
        val rs = statement.executeQuery("SELECT 1")
        assert(rs != null);
        connection.close()
    }


}
