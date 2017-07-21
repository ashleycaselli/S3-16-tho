import org.scalatest.FunSuite

class ClueTest extends FunSuite {
    val clueText = "if you want to find the white bunny, go to the hospital and ask for your granny"
    val clue = new ClueImpl(clueText)

    test("clue must be a String") {
        assert(clue.getClue.isInstanceOf[String])
    }
}
