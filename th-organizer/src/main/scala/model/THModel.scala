package model

import com.lynden.gmapsfx.javascript.`object`.Marker
import core.Observable
import domain._
import domain.messages._
import play.api.libs.json.Json

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

trait THModel extends Observable[String] {

    def broker: Broker

    def broker_=(broker: Broker): Unit

    def addTreasureHunt(th: TreasureHunt): Unit

    def getTreasureHunts: List[TreasureHunt]

    def addPOI(poi: POI): Unit

    def addPoiMarker(poiMarker: Marker, poi: POI): Unit

    def deletePOI(poi: POI): Unit

    def getPOIs: Seq[POI]

    def getPoiMarker(poi: POI): Option[Marker]

    def getCode: Int

    def startHunt(): Unit

    def stopHunt(): Unit

    def isTHRunning(): Boolean

    def setRunningTH(ID: Int)
}

class THModelImpl(override var broker: Broker) extends THModel {

    require(broker != null)

    private val organizerID = "1"
    private var runningTH: TreasureHunt = null
    private var THStarted = false //TODO ask to server
    private var ths: List[TreasureHunt] = List empty
    private var pois: ListBuffer[POI] = ListBuffer empty
    private val markerList: mutable.HashMap[POI, Marker] = mutable.HashMap.empty

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
        val ID = (broker call thMsg).toInt
        th.ID = ID
        ths = ths :+ th
        setRunningTH(th.ID)
        notifyObservers(TreasureHuntMsgImpl(organizerID, th defaultRepresentation).defaultRepresentation)
    }

    override def getTreasureHunts: List[TreasureHunt] = ths

    override def addPOI(poi: POI): Unit = {
        require(pois != null && !pois.contains(poi))
        val poiMsg = PoiMsgImpl(organizerID, poi defaultRepresentation).defaultRepresentation
        val message = toMessage(broker call poiMsg)
        val poiWithIDs = Json.parse(message.payload).as[POI]
        pois = pois :+ poiWithIDs
        notifyObservers(poiMsg)
    }

    override def addPoiMarker(poiMarker: Marker, poi: POI): Unit = {
        markerList.put(poi, poiMarker)
    }

    override def deletePOI(poi: POI): Unit = {
        require(pois.contains(poi))
        val poiMsg = PoiMsgImpl(organizerID, poi defaultRepresentation).defaultRepresentation
        broker send poiMsg
        pois -= poi
        markerList.remove(poi)
    }

    override def getPoiMarker(poi: POI): Option[Marker] = {
        markerList.get(poi)
    }

    override def getPOIs: Seq[POI] = {
        if (pois.isEmpty) {
            pois = getPOIsFromDB
        }
        pois
    }

    def getPOIsFromDB: ListBuffer[POI] = {
        val listMsg = broker.call(ListPOIsMsgImpl(organizerID, runningTH.ID.toString).defaultRepresentation)
        Json.parse(toMessage(listMsg).payload).as[ListPOIs].list
    }

    override def getCode: Int = runningTH.ID

    override def startHunt(): Unit = {
        require(runningTH != null)
        broker send (StateMsgImpl(organizerID, StateImpl(StateType.Start, runningTH.ID).defaultRepresentation) defaultRepresentation)
        THStarted = true
    }

    override def stopHunt(): Unit = {
        require(runningTH != null)
        broker send (StateMsgImpl(organizerID, StateImpl(StateType.Stop, runningTH.ID).defaultRepresentation) defaultRepresentation)
        THStarted = false
    }

    override def setRunningTH(ID: Int): Unit = {
        for (th <- ths) {
            if (th.ID == ID) {
                runningTH = th
            }
        }
    }

    override def isTHRunning(): Boolean = {
        THStarted
    }

    def toMessage(string: String): Message = Json.parse(string).as[Message]
}