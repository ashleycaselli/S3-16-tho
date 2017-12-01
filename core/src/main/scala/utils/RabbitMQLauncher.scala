package utils

object RabbitMQLauncher {

    def main(args: Array[String]): Unit = {
        val server = RabbitMQServer.apply
        server run
    }

}


