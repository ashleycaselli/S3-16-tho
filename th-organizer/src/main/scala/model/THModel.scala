package model

import core.Observable
import domain._
import domain.messages.{PoiMsgImpl, StateMsgImpl, StateType, TreasureHuntMsgImpl}

import scala.language.postfixOps

trait THModel extends Observable[String] {

    def broker: Broker

    def broker_=(broker: Broker): Unit

    def addTreasureHunt(th: TreasureHunt): Unit

    def getTreasureHunts: Seq[TreasureHunt]

    def addQuiz(quiz: Quiz): Unit

    def getQuizzes: Seq[Quiz]

    def addPOI(poi: POI): Unit

    def getPOIs: Seq[POI]

    def getCode: Int

    def startHunt(): Unit
}

class THModelImpl(override var broker: Broker) extends THModel {

    require(broker != null)

    private val organizerID = ""
    private var runningTH: TreasureHunt = _

    private var ths: Seq[TreasureHunt] = Seq empty
    private var quizzes: Seq[Quiz] = Seq empty
    private var pois: Seq[POI] = Seq empty

    override def addTreasureHunt(th: TreasureHunt): Unit = {
        require(ths != null && !ths.contains(th))
        val thMsg = TreasureHuntMsgImpl(organizerID, th defaultRepresentation).defaultRepresentation
        val ID = broker call thMsg
        setRunningTH(ID)
        ths = ths :+ th
        notifyObservers(thMsg)
    }

    override def getTreasureHunts: Seq[TreasureHunt] = ths

    override def addQuiz(quiz: Quiz): Unit = {
        require(quizzes != null && !quizzes.contains(quiz))
        quizzes = quizzes :+ quiz
    }

    override def getQuizzes: Seq[Quiz] = quizzes

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

}