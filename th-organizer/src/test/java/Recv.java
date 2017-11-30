import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {

    private final static String QUEUE_NAME = "rpc_queue";
    private final static String EXCHANGE_NAME = "organizer-message";
    private String lastMessage = null;

    public void startRecv(String response) throws Exception {
        System.out.println("recv running");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("52.14.140.101");
        factory.setUsername("test");
        factory.setPassword("test");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                channel.basicPublish("", properties.getReplyTo(), new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId()).build(), response.getBytes());
                lastMessage = new String(body, "UTF-8");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);


        Channel channelPS = connection.createChannel();
        channelPS.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channelPS.queueDeclare().getQueue();
        channelPS.queueBind(queueName, EXCHANGE_NAME, "");
        Consumer consumer2 = new DefaultConsumer(channelPS) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                lastMessage = message;
            }
        };
        channelPS.basicConsume(queueName, true, consumer2);
    }

    public String getLastMessage() {
        return lastMessage;
    }
}