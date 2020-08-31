package fun.mytoys.rabbitmq.rabbitmq.chap_04;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("Duplicates")
public class T012_RabbitProducer_QueueTTL {
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

        Map<String, Object> queueArgs = new HashMap<>();
        queueArgs.put("x-expires", 10000);

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, queueArgs);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
                null,
                "mandatory test".getBytes());

        channel.close();
        connection.close();
    }
}
