package receiver

import org.scalatest.{BeforeAndAfter, FunSuite}

class ReceiverTest extends FunSuite with BeforeAndAfter {
    private var sender: Send = null
    private var receiver: Receiver = null

    before {
        sender = new Send
        receiver = new ReceiverImpl
        receiver.startRecv
    }

    test("TH message test") {
        Thread.sleep(5000)
        val msg = sender.callTreasureHunt()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 5000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("Position message test") {
        Thread.sleep(5000)
        val msg = sender.sendPosition()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 5000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("Clue message test") {
        Thread.sleep(5000)
        val msg = sender.sendClue()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 5000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("Quiz message test") {
        Thread.sleep(5000)
        val msg = sender.sendQuiz()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 5000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("Poi message test") {
        Thread.sleep(5000)
        val msg = sender.sendPoi()
        var pass5: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass5 == false && System.currentTimeMillis - timestamp < 5000) {
            if (receiver.getLastMessage() == msg) {
                pass5 = true
            }
        }
        if (pass5 == true) assert(true)
        else assert(false);
    }

    test("State message test") {
        Thread.sleep(5000)
        val message = sender.sendState()
        var pass4: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass4 == false && System.currentTimeMillis - timestamp < 5000) {
            if (receiver.getLastMessage() == message) {
                pass4 = true
            }
        }
        if (pass4 == true) assert(true)
        else assert(false);
    }

}