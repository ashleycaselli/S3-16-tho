package receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import domain.*;
import domain.messages.*;
import utils.RabbitInfo;

class Send {

    ConnectionFactory factory;
    Connection connection;
    Channel channel;

    Send() throws Exception {
        this.factory = new ConnectionFactory();
        this.factory.setHost(RabbitInfo.HOST());
        this.connection = this.factory.newConnection();
        this.channel = this.connection.createChannel();
        this.channel.queueDeclare(RabbitInfo.QUEUE_NAME(), false, false, false, null);
    }

    String callListTHs() throws Exception {
        String message = new ListTHsMsgImpl("sender", "{\"list\":[{\"ID\":0,\"name\":\"nome\",\"location\":\"loc\",\"date\":\"date\",\"time\":\"time\"},{\"ID\":0,\"name\":\"nome\",\"location\":\"loc\",\"date\":\"date\",\"time\":\"time\"}]}").defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message.getBytes());
        return message;
    }

    String callTreasureHunt() throws Exception {
        String message1 = new TreasureHuntMsgImpl("sender", new TreasureHuntImpl(0, "name", "location", "2017-09-04", "time", null).defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message1.getBytes());
        return message1;
    }

    String sendClue() throws Exception {
        String message1 = new ClueMsgImpl("sender", new ClueImpl("contenuto").defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message1.getBytes());
        return message1;
    }

    String sendQuiz() throws Exception {
        String message2 = new QuizMsgImpl("sender", new QuizImpl("domanda", "risposta").defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message2.getBytes());
        return message2;
    }

    String sendPosition() throws Exception {
        String message3 = new PositionMsgImpl("sender", new PositionImpl(43.46353, 46.64539).defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message3.getBytes());
        return message3;
    }

    String sendState() throws Exception {
        String message4 = new StateMsgImpl("sender", new StateImpl(StateType.Created(), 0).defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message4.getBytes());
        return message4;
    }

    String sendPoi() throws Exception {
        POI poi = new POIImpl(new PositionImpl(44.55432, 45.83654), "test POI", "thID", new QuizImpl("question", "answer"), new ClueImpl("clue"));
        String message = new PoiMsgImpl("sender", poi.defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message.getBytes());
        return message;
    }
}
