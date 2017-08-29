package receiver

import org.scalatest.FunSuite

class ReceiverTest extends FunSuite {
    private var sender: Send = null
    private var receiver: Receiver = null


    sender = new Send()
    receiver = new ReceiverImpl
    receiver.startRecv

    test("Position message test") {
        Thread.sleep(2000)
        val msg = sender.sendPosition()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 2000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("Clue message test") {
        Thread.sleep(2000)
        val msg = sender.sendClue()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 2000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("Quiz message test") {
        Thread.sleep(2000)
        val msg = sender.sendQuiz()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 2000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }

    test("Poi message test") {
        Thread.sleep(2000)
        val msg = sender.sendPoi()
        var pass: Boolean = false
        val timestamp: Long = System.currentTimeMillis
        while (pass == false && System.currentTimeMillis - timestamp < 2000) {
            if (receiver.getLastMessage() == msg) {
                pass = true
            }
        }
        if (pass == true) assert(true)
        else assert(false);
    }
}