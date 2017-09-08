package model

import core.Observable
import domain._
import domain.messages.StateType.StateType
import domain.messages._
import domain.messages.msgType.msgType
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsArray, JsPath, Json, Reads}

import scala.language.postfixOps

trait THModel extends Observable[String] {

    def broker: Broker

    def broker_=(broker: Broker): Unit

    def addTreasureHunt(th: TreasureHunt): Unit

    def getTreasureHunts: List[TreasureHunt]

    def addPOI(poi: POI): Unit

    def getPOIs: Seq[POI]

    def getCode: Int

    def startHunt(): Unit
}

class THModelImpl(override var broker: Broker) extends THModel {

    implicit val messageReads: Reads[Message] = (
      (JsPath \ "messageType").read[msgType] and
        (JsPath \ "sender").read[String] and
        (JsPath \ "payload").read[String]
      ) (Message.apply _)


    implicit val positionReads: Reads[Position] = (
      (JsPath \ "latitude").read[Double] and
        (JsPath \ "longitude").read[Double]
      ) (Position.apply _)

    implicit val quizReads: Reads[Quiz] = (
      (JsPath \ "question").read[String] and
        (JsPath \ "answer").read[String]
      ) (Quiz.apply _)

    implicit val clueReads: Reads[Clue] =
        (JsPath \ "content").read[String].map(Clue.apply _)

    implicit val poiReads: Reads[POI] = (
            (JsPath \ "name").read[String] and
                    (JsPath \ "treasureHuntID").read[Int] and
                    (JsPath \ "position").read[String] and
                    (JsPath \ "quiz").read[String] and
                    (JsPath \ "clue").read[String]
            ) (POI.apply _)

    implicit val stateReads: Reads[State] = (
      (JsPath \ "state").read[StateType] and
        (JsPath \ "treasureHuntID").read[Int]
      ) (State.apply _)

    implicit val thReads: Reads[TreasureHunt] = (
      (JsPath \ "ID").read[Int] and
        (JsPath \ "name").read[String] and
        (JsPath \ "location").read[String] and
        (JsPath \ "date").read[String] and
        (JsPath \ "time").read[String]
      ) (TreasureHunt.apply _)

    implicit val listTHsReads: Reads[ListTHs] =
        (JsPath \ "list").read[JsArray].map(ListTHs.apply _)

    implicit val listPOIsReads: Reads[ListPOIs] =
        (JsPath \ "list").read[JsArray].map(ListPOIs.apply _)

    require(broker != null)

    private val organizerID = ""
    private var runningTH: TreasureHunt = _

    private var ths: List[TreasureHunt] = List empty
    private var pois: List[POI] = List empty

    new Thread() {
        override def run() {
            ths = Json.parse(toMessage(broker.call(ListTHsMsgImpl(organizerID, ListTHsImpl(List.empty[TreasureHunt]).defaultRepresentation).defaultRepresentation)).payload).as[ListTHs].list
        }
    }.start()

    override def addTreasureHunt(th: TreasureHunt): Unit = {
        require(ths != null && !ths.contains(th))
        val thMsg = TreasureHuntMsgImpl(organizerID, th defaultRepresentation).defaultRepresentation
        val ID = broker call thMsg
        setRunningTH(ID)
        ths = ths :+ th
        notifyObservers(thMsg)
    }

    override def getTreasureHunts: List[TreasureHunt] = ths

    override def addPOI(poi: POI): Unit = {
        require(pois != null && !pois.contains(poi))
        broker send (PoiMsgImpl(organizerID, poi defaultRepresentation) defaultRepresentation)
        pois = pois :+ poi
    }

    override def getPOIs: Seq[POI] = pois

    override def getCode: Int = runningTH.ID

    override def startHunt(): Unit = {
        require(runningTH != null)
        broker send (StateMsgImpl(organizerID, StateImpl(StateType.Start, runningTH.ID).defaultRepresentation) defaultRepresentation)
    }

    def setRunningTH(ID: String): Unit = {
        for (th <- ths) {
            if (th.ID == ID) {
                runningTH = th
            }
        }
    }

    def toMessage(string: String): Message = {
        Json.parse(string).as[Message]
    }

}