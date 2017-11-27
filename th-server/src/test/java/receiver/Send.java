package receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import domain.*;
import domain.messages.*;
import utils.RabbitInfo;

public class Send {

    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    private final String sender = "0";

    public Send() throws Exception {
        this.factory = new ConnectionFactory();
        this.factory.setHost(RabbitInfo.HOST());
        this.factory.setUsername(RabbitInfo.USERNAME());
        this.factory.setPassword(RabbitInfo.PASSWORD());
        this.connection = this.factory.newConnection();
        this.channel = this.connection.createChannel();
        this.channel.queueDeclare(RabbitInfo.QUEUE_NAME(), false, false, false, null);
    }

    public String callListTHs() throws Exception {
        String message = new ListTHsMsgImpl(this.sender, "{\"list\":[{\"ID\":0,\"name\":\"nome\",\"location\":\"loc\",\"date\":\"date\",\"time\":\"time\"},{\"ID\":0,\"name\":\"nome\",\"location\":\"loc\",\"date\":\"date\",\"time\":\"time\"}]}").defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message.getBytes());
        return message;
    }

    public String callTreasureHunt() throws Exception {
        String message1 = new TreasureHuntMsgImpl(this.sender, new TreasureHuntImpl(1, "name", "location", "2017-09-04", "time", null).defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message1.getBytes());
        return message1;
    }

    public String sendClue() throws Exception {
        String message1 = new ClueMsgImpl(this.sender, new ClueImpl(0, "contenuto").defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message1.getBytes());
        return message1;
    }

    public String sendQuiz() throws Exception {
        String message2 = new QuizMsgImpl(this.sender, new QuizImpl(0, "domanda", "risposta").defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message2.getBytes());
        return message2;
    }

    public String sendPosition() throws Exception {
        String message3 = new PositionMsgImpl(this.sender, new PositionImpl(43.46353, 46.64539).defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message3.getBytes());
        return message3;
    }

    public String sendState() throws Exception {
        String message4 = new StateMsgImpl(this.sender, new StateImpl(StateType.Created(), 0).defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message4.getBytes());
        return message4;
    }

    public String sendPoi() throws Exception {
        POI poi = new POIImpl(0, new PositionImpl(44.55432, 45.83654), "test POI", 0, new QuizImpl(0, "question", "answer"), new ClueImpl(0, "clue"));
        String message = new PoiMsgImpl("sender", poi.defaultRepresentation()).defaultRepresentation();
        this.channel.basicPublish(RabbitInfo.EXCHANGE_NAME(), "", null, message.getBytes());
        return message;
    }

}
