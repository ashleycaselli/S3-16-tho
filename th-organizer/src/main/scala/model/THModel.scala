package model

import core.Observable
import domain._
import domain.messages._
import play.api.libs.json.Json

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

    require(broker != null)

    private val organizerID = "1"
    private var runningTH: TreasureHunt = _

    private var ths: List[TreasureHunt] = List empty
    private var pois: List[POI] = List empty

    new Thread() {
        override def run() {
            val listMsg = broker.call(ListTHsMsgImpl(organizerID, ListTHsImpl(List.empty[TreasureHunt]).defaultRepresentation).defaultRepresentation)
            ths = Json.parse(toMessage(listMsg).payload).as[ListTHs].list
            notifyObservers(listMsg)
        }
    }.start()

    override def addTreasureHunt(th: TreasureHunt): Unit = {
        require(ths != null && !ths.contains(th))
        val thMsg = TreasureHuntMsgImpl(organizerID, th defaultRepresentation).defaultRepresentation
        val ID = broker call thMsg
        th.ID = ID.toInt
        ths = ths :+ th
        setRunningTH(ID.toInt)
        notifyObservers(thMsg)
    }

    override def getTreasureHunts: List[TreasureHunt] = ths

    override def addPOI(poi: POI): Unit = {
        require(pois != null && !pois.contains(poi))
        val poiMsg = PoiMsgImpl(organizerID, poi defaultRepresentation).defaultRepresentation
        broker send poiMsg
        pois = pois :+ poi
        notifyObservers(poiMsg)
    }

    override def getPOIs: Seq[POI] = pois

    override def getCode: Int = runningTH.ID

    override def startHunt(): Unit = {
        require(runningTH != null)
        broker send (StateMsgImpl(organizerID, StateImpl(StateType.Start, runningTH.ID).defaultRepresentation) defaultRepresentation)
    }

    def setRunningTH(ID: Int): Unit = {
        for (th <- ths) {
            if (th.ID == ID) {
                runningTH = th
            }
        }
    }

    def toMessage(string: String): Message = Json.parse(string).as[Message]

}