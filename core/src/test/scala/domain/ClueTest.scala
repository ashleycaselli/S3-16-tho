package domain

import com.typesafe.scalalogging.Logger
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

class ClueTest extends FunSuite with BeforeAndAfter {

    private val clueText = "If you want to find the white bunny, go to the hospital and ask for your granny."
    private var clue: Clue = null
    val logger = Logger(LoggerFactory.getLogger("test"))

    before {
        clue = ClueImpl(clueText)
    }

    test("Clue content must be a String") {
        assert(clue.content.isInstanceOf[String])
    }

    test("Clue content checking") {
        assert(clue.content === clueText)
    }

    test("Clue content checking after a new value has been set up") {
        val newClueText =
            "I have a face but no nose, eyes or mouth. I have numbers on me"
        clue.content = newClueText
        assert(clue.content === newClueText)
    }

    test("Checking Clue's serializability") {
        implicit val clueReads = Json.reads[ClueImpl]
        val newClue = Json.parse(clue.defaultRepresentation).as[ClueImpl]
        logger.info(s"oldClue: ${clue.defaultRepresentation.toString}")
        logger.info(s"newClue: ${newClue.defaultRepresentation.toString}")
        assert(clue === newClue)
    }

}
