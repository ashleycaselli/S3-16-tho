package utils

import com.typesafe.scalalogging.Logger

import scala.sys.process._

sealed trait RabbitMQServer {
    def path: String

    def run: Unit = {
        logger.info("RabbitMQ Server is ready to run")
        new Thread {
            override def run(): Unit = {
                path.!
            }
        }.start
    }

    def stopCommand: String

    def stop: Unit = {
        stopCommand.!
        logger.info("RabbitMQ Server stopped")
    }

    val logger = Logger[RabbitMQServer]
}

object RabbitMQServer {
    val currentOS = System.getProperty("os.name")

    def apply: RabbitMQServer = currentOS match {
        case x if x contains "Mac" => MacRabbitMQ()
        case x if x contains "Windows" => WinRabbitMQ()
        case x => throw new NotFoundOSException(x concat " not expected")
    }
}

case class WinRabbitMQ() extends RabbitMQServer {
    override def path = "C:\\Program Files\\RabbitMQ Server\\rabbitmq_server-3.6.10\\sbin\\rabbitmq-server.bat"

    // TODO Windows - stop the execution of RabbitMQ Server
    override def stopCommand = ???
}

case class MacRabbitMQ() extends RabbitMQServer {
    override def path: String = "/usr/local/sbin/rabbitmq-server"

    override def stopCommand = "rabbitmqctl stop"
}

case class NotFoundOSException(s: String) extends Exception(s)
