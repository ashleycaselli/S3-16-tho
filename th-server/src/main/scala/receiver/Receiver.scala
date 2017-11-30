package receiver

import java.io.IOException

import com.rabbitmq.client._
import dboperation._
import domain._
import domain.messages._
import play.api.libs.json.Json
import utils.RabbitInfo

trait Receiver {

    def startRecv(): Unit

    def getLastMessage: String
}

class ReceiverImpl extends Receiver {

    private var lastMessage: String = _
    private var factory: ConnectionFactory = _
    private var connection: Connection = _
    private var winChannel: Channel = _

    @throws[Exception]
    override def startRecv(): Unit = {
        println("Receiver started")
        factory = new ConnectionFactory
        factory setHost RabbitInfo.HOST
        factory setUsername RabbitInfo.USERNAME
        factory setPassword RabbitInfo.PASSWORD
        connection = factory.newConnection

        startWinChannel()
        val channel = connection.createChannel
        channel.queueDeclare(RabbitInfo.QUEUE_NAME, false, false, false, null)
        val organizerConsumer = new DefaultConsumer(channel) {
            @throws[IOException]
            override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
                val data = new String(body, RabbitInfo.MESSAGE_ENCODING)
                println("received: " + body)
                lastMessage = data
                println("received: " + data)
                val message = Json.parse(data).as[Message]
                val sender = message.sender toInt
                val mType = message.messageType
                val payload = message.payload
                mType match {
                    case msgType.TreasureHunt => {
                        val idOrganizer = sender
                        val th = Json.parse(payload).as[TreasureHunt]
                        val treasureHuntDB: TreasureHuntDB = new TreasureHuntDBImpl

                        if (th.ID == 0) {
                            val thID = treasureHuntDB.insertNewTreasureHunt(th.name, th.location, th.date, th.time, idOrganizer)
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, thID.toString.getBytes)
                        } else {

                        }
                    }
                    case msgType.ListTHs => {
                        val idOrganizer = sender
                        val treasureHuntDB = new TreasureHuntDBImpl
                        val list = treasureHuntDB.viewTreasureHuntListByOrganizer(idOrganizer)
                        val message: String = ListTHsMsgImpl("0", ListTHsImpl(list).defaultRepresentation).defaultRepresentation
                        channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, message.getBytes)
                    }
                    case msgType.ListPOIs => {
                        val poiDB = new POIDBImpl
                        val list = poiDB.getPOIsList(payload.toInt)
                        val message: String = new ListPOIsMsgImpl("0", new ListPOIsImpl(list).defaultRepresentation).defaultRepresentation
                        channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, message.getBytes)
                    }
                    case msgType.Poi => {

                        val idOrganizer = sender
                        val poi = Json.parse(payload).as[POI]
                        val poiDB: POIDB = new POIDBImpl

                        if (poi.ID.toInt == 0) {
                            val newQuiz: NewQuizDB = new NewQuizDBImpl
                            val newClue: NewClueDB = new NewClueDBImpl
                            val idQuiz = newQuiz.insertNewQuiz(poi.quiz.question, poi.quiz.answer, idOrganizer)
                            val idClue = newClue.insertNewClue(poi.clue.content, idOrganizer)
                            val idPOI = poiDB.insertNewPOI(poi, idOrganizer)

                            poiDB.setQuiz(idPOI, idQuiz)
                            poiDB.setClue(idPOI, idClue)

                            poi.ID = idPOI
                            poi.quiz.ID = idQuiz
                            poi.clue.ID = idClue

                            val message: String = PoiMsgImpl("0", poi.defaultRepresentation).defaultRepresentation
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, message.getBytes)
                        } else {
                            poiDB.deletePoi(poi)
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, RabbitInfo.OK_RESPONSE.getBytes)
                        }

                    }
                    case msgType.State => {
                        val idOrganizer = sender
                        val stateOfTH = Json.parse(payload).as[State]
                        if (stateOfTH.state == StateType.Start) {
                            val eventDB: EventDB = EventDBImpl()
                            eventDB.organizerStartTreasureHunt(stateOfTH.treasureHuntID, idOrganizer)
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, RabbitInfo.OK_RESPONSE.getBytes)
                        } else {
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, RabbitInfo.KO_RESPONSE.getBytes)
                        }
                    }
                    case _ => {
                        channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, RabbitInfo.KO_RESPONSE.getBytes)
                    }
                }
            }
        }

        channel.basicConsume(RabbitInfo.QUEUE_NAME, true, organizerConsumer)


        val channelPS = connection.createChannel
        channelPS.exchangeDeclare(RabbitInfo.EXCHANGE_NAME, "fanout")
        val queueName = channelPS.queueDeclare.getQueue
        channelPS.queueBind(queueName, RabbitInfo.EXCHANGE_NAME, "")
        val playerConsumer = new DefaultConsumer(channelPS) {
            @throws[IOException]
            override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
                val data = new String(body, RabbitInfo.MESSAGE_ENCODING)
                lastMessage = data
                println(data)
                val message = Json.parse(data).as[Message]
                val sender = message.sender
                val mType = message.messageType
                val payload = message.payload
                mType match {
                    case msgType.Poi => {
                        val poi = Json.parse(payload).as[POI]
                        val poiDB: POIDB = new POIDBImpl
                        if (poi.ID == 0) {
                            val nextPoi = poiDB getFirstPoi (poi.treasureHuntID)

                            //iscrivo la squadra alla caccia al tesoro
                            val teamDB: TeamDB = TeamDBImpl()
                            val idTeam = teamDB.subscribeTreasureHunt(poi.treasureHuntID, poi.name)
                            //faccio iniziare a quella squadra la sua caccia al tesoro
                            val eventDB: EventDB = EventDBImpl()
                            eventDB.teamStartTreasureHunt(poi.treasureHuntID, idTeam)
                            //il player riceve il quiz
                            eventDB.teamReceiveQuiz(nextPoi.treasureHuntID, idTeam, nextPoi.quiz.ID, " Quiz del Primo POI")

                            val message: String = PoiMsgImpl(idTeam.toString, nextPoi.defaultRepresentation).defaultRepresentation
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, message.getBytes())

                        } else {
                            var poi = Json.parse(payload).as[POI]
                            val nextPoi = poiDB getSubsequentPoi (poi)
                            if (nextPoi != null) {
                                val message: String = PoiMsgImpl(sender, nextPoi.defaultRepresentation).defaultRepresentation
                                channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, message.getBytes())
                                val eventDB: EventDB = EventDBImpl()
                                eventDB.teamReachPOI(poi.treasureHuntID, sender.toInt, poi.ID, "Raggiunto un POI")
                                eventDB.teamReceiveQuiz(nextPoi.treasureHuntID, sender.toInt, nextPoi.quiz.ID, "Quiz di un POI Generico")
                            } else {
                                val eventDB: EventDB = EventDBImpl()
                                eventDB.teamReachPOI(poi.treasureHuntID, sender.toInt, poi.ID, "Raggiunto l'ultimo POI")
                                //avviso tutti i giocatore che c'è stato un vincitore
                                win(sender.toInt, poi.treasureHuntID)
                            }
                        }
                    }
                    case msgType.TreasureHunt => { // received if organizer creates a new TreasureHunt
                        var th = Json.parse(payload).as[TreasureHunt]
                        val treasureHuntDB: TreasureHuntDB = new TreasureHuntDBImpl
                        val thID = treasureHuntDB.insertNewTreasureHunt(th.name, th.location, th.date, th.time, sender.toInt)
                    }
                    case msgType.ListTHs => { // received if player require TH list
                        val treasureHuntDB = new TreasureHuntDBImpl
                        val list = treasureHuntDB.viewTreasureHuntListByPlayer()
                        val message: String = ListTHsMsgImpl("0", ListTHsImpl(list).defaultRepresentation).defaultRepresentation
                        channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, message.getBytes)
                    }
                    case msgType.Login => {
                        var login = Json.parse(payload).as[Login]
                        var teamName = login.username
                        var password = login.password

                        val teamDB: TeamDB = new TeamDBImpl
                        if (teamDB.login(teamName, password)) {
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, ("{\"messageType\":\"LoginMsg\",\"sender\":\"0\",\"payload\":\"" + RabbitInfo.OK_RESPONSE + "\"}").getBytes())
                        } else {
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, ("{\"messageType\":\"LoginMsg\",\"sender\":\"0\",\"payload\":\"" + RabbitInfo.KO_RESPONSE + "\"}").getBytes())
                        }
                    }
                    case msgType.Registration => {
                        var reg = Json.parse(payload).as[Registration]
                        var teamName = reg.username
                        var password = reg.password

                        val teamDB: TeamDB = new TeamDBImpl
                        if (teamDB.checkTeamNameAvailability(teamName)) {
                            teamDB.createNewTeam(teamName, password)
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, ("{\"messageType\":\"RegistrationMsg\",\"sender\":\"0\",\"payload\":\"" + RabbitInfo.OK_RESPONSE + "\"}").getBytes())
                        } else {
                            channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, ("{\"messageType\":\"RegistrationMsg\",\"sender\":\"0\",\"payload\":\"" + RabbitInfo.KO_RESPONSE + "\"}").getBytes())
                        }
                    }
                    case msgType.Clue => {
                        val teamID = sender.toInt
                        val payloadIDs = payload.split("-")
                        val clueID = payloadIDs(0).toInt
                        val thID = payloadIDs(1).toInt

                        val eventDB: EventDB = EventDBImpl()
                        eventDB.teamReceiveClue(thID, teamID, clueID)
                    }
                    case msgType.Unsubscription => {
                        val teamID = sender.toInt
                        val treasureHuntID = payload.toInt

                        val teamDB: TeamDB = TeamDBImpl()
                        teamDB.unsubscribeTreasureHunt(treasureHuntID, teamID)
                    }
                    case _ => {
                        channel.basicPublish("", properties.getReplyTo, new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId).build, RabbitInfo.KO_RESPONSE.getBytes)
                    }
                }
            }
        }

        channelPS.basicConsume(queueName, false, playerConsumer)
    }

    override def getLastMessage: String = lastMessage

    private def win(teamID: Int, treasureHuntID: Int): Unit = {
        val eventDB: EventDB = EventDBImpl()
        val teamName = eventDB.teamFinishTreasureHunt(treasureHuntID, teamID, "Win")
        val message = WinMsgImpl(teamName, treasureHuntID.toString).defaultRepresentation
        winChannel.basicPublish("winExchange", "", null, message.getBytes)
    }

    private def startWinChannel() = {
        winChannel = connection.createChannel
        winChannel.exchangeDeclare("winExchange", "direct", true)
        winChannel.queueDeclare("winQueue", true, false, false, null)
        winChannel.queueBind("winQueue", "winExchange", "")
    }

}
