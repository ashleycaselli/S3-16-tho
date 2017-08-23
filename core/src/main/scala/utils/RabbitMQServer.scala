package utils

import com.typesafe.scalalogging.Logger

import scala.sys.process._

/**
  * RabbitMQServer is a component that represents the RabbitMQ service required for create application Rabbit-based
  */
sealed trait RabbitMQServer {

    /**
      * Property that contains the path of the runnable file
      *
      * @return runnable file's absolute path
      */
    def path: String

    /**
      * This method is used to run the RabbitMQ Server
      */
    def run: Unit = {
        logger info "RabbitMQ Server is ready to run"
        new Thread {
            override def run(): Unit = path.!
        } start
    }

    /**
      * Property that contains the command to stop the RabbitMQ Server
      *
      * @return a String containing the stop command
      */
    def stopCommand: String

    /**
      * This method performs a graceful stop of the RabbitMQ Server
      */
    def stop: Unit = {
        stopCommand.!
        logger info "RabbitMQ Server stopped"
    }

    val logger = Logger[RabbitMQServer]
}

/**
  * Object that use the factory pattern to create a concrete instance of RabbitMQServer based on underlying OS
  */
object RabbitMQServer {
    val currentOS = System getProperty "os.name"

    def apply: RabbitMQServer = currentOS match {
        case x if x contains "Mac" => MacRabbitMQ()
        case x if x contains "Windows" => WinRabbitMQ()
        case x => throw new NotFoundOSException(x concat " not expected")
    }
}

/**
  * RabbitMQ Server running on Windows platforms
  */
case class WinRabbitMQ() extends RabbitMQServer {
    override def path = "C:\\Program Files\\RabbitMQ Server\\rabbitmq_server-3.6.10\\sbin\\rabbitmq-server.bat"

    // TODO Windows - stop the execution of RabbitMQ Server
    override def stopCommand = ???
}

/**
  * RabbitMQ Server running on Mac OS platforms
  */
case class MacRabbitMQ() extends RabbitMQServer {
    override def path: String = "/usr/local/sbin/rabbitmq-server"

    override def stopCommand = "rabbitmqctl stop"
}

/**
  * Exception raised to indicate that the current OS is not expected
  *
  * @param s exception's description
  */
case class NotFoundOSException(val s: String) extends Exception(s)
