package fun.mytoys.rabbitmq.rabbitmq.chap_04;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("Duplicates")
public class T012_RabbitProducer_MessageTTLMessage {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routekey_demo";
    private static final String BINDING_KEY = "routekey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.30.174";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false,
                new AMQP.BasicProperties.Builder()
                        .deliveryMode(2)
                        .expiration("6000").build(),
                "mandatory test".getBytes());

        channel.close();
        connection.close();
    }
}
