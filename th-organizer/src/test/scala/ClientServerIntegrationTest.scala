import model.ModelBroker
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import utils.RabbitMQServer

class ClientServerIntegrationTest extends FunSuite with BeforeAndAfterAll {

    val rabbit = RabbitMQServer.apply

    override protected def beforeAll(): Unit = {
        rabbit.run
        Thread sleep 10000
    }

    test("Client-Server") {
        new ServerDoubleTest

        val message = "ciao"

        val broker = new ModelBroker
        println(" [x] Requesting echo(\"ciao\")")
        val response = broker.call(message)
        println(s" [.] Got ${response}")

        broker.close

        assert(message === response)
    }

    override protected def afterAll(): Unit = rabbit.stop
}
