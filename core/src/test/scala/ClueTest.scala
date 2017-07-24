import org.scalatest.{BeforeAndAfter, FunSuite}

class ClueTest extends FunSuite with BeforeAndAfter {

    private val clueText = "If you want to find the white bunny, go to the hospital and ask for your granny."
    private var clue: Clue = null

    before {
        clue = ClueImpl(clueText)
    }

    test("Clue content must be a String") {
        assert(clue.clue.isInstanceOf[String])
    }

    test("Clue content checking") {
        assert(clue.clue === clueText)
    }

    test("Clue content checking after a new value has been set up") {
        val newClueText =
            "I have a face but no nose, eyes or mouth. I have numbers on me"
        clue.clue = newClueText
        assert(clue.clue === newClueText)
    }

}
