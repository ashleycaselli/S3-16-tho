import org.scalatest.{BeforeAndAfter, FunSuite}


class ModelBrokerTest extends FunSuite with BeforeAndAfter {

    private var recv: Recv = null
    private val broker: ModelBroker = new ModelBroker
    private val messageToCall: String = "messageToCall"
    private val response: String = "response"
    private val messageToSend: String = "messageToSend"

    before {
        recv = new Recv
        recv.startRecv(response)
    }

    test("call message typo test") {
        assert(broker.call(messageToCall).isInstanceOf[String])
    }

    test("call message content test") {
        assert(broker.call(messageToCall) == response)
    }

    test("send message typo test") {
        broker.send(messageToSend)
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 10000) {
            if (recv.getLastMessage().isInstanceOf[String]) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("send message content test") {
        broker.send(messageToSend)
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 10000) {
            if (recv.getLastMessage() == messageToSend) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }
}
