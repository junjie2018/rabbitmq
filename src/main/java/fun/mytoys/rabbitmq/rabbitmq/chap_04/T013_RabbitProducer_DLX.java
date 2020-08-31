package fun.mytoys.rabbitmq.rabbitmq.chap_04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


@SuppressWarnings("Duplicates")
public class T013_RabbitProducer_DLX {
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
        queueArgs.put("x-message-ttl", 5000);
        queueArgs.put("x-dead-letter-exchange", "dlxExchange");

        // 定义一个配置了ttl及死信队列的队列
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("ttlExchange", "direct", true, false, null);
        channel.queueDeclare("ttlQueue", true, false, false, queueArgs);
        channel.queueBind("ttlQueue", "ttlExchange", BINDING_KEY);

        channel.exchangeDeclare("dlxExchange", "fanout", true, false, null);
        channel.queueDeclare("dlxQueue", true, false, false, null);
        channel.queueBind("dlxQueue", "dlxExchange", BINDING_KEY);


        channel.basicPublish("ttlExchange", ROUTING_KEY,
                null,
                "mandatory test".getBytes());

        channel.close();
        connection.close();
    }
}
