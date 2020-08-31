package fun.mytoys.rabbitmq.rabbitmq.chap_04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("Duplicates")
public class T003_RabbitProducer_AlternateExchange {
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

        Map<String, Object> exchangeArgs = new HashMap<>();
        exchangeArgs.put("alternate-exchange", "myAlternateExchange");

        Channel channel = connection.createChannel();

        // 正常的交换机
        channel.exchangeDeclare("normalExchange", "direct", true, false, exchangeArgs);
        channel.queueDeclare("normalQueue", true, false, false, null);
        channel.queueBind("normalQueue", "normalExchange", "normalKey");

        // 备份的交换机
        channel.exchangeDeclare("myAlternateExchange", "fanout", true, false, null);
        channel.queueDeclare("unRoutedQueue", true, false, false, null);
        channel.queueBind("unRoutedQueue", "myAlternateExchange", "");

        channel.basicPublish("normalExchange", "normalKey", false,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                "test alternate-exchange normal".getBytes());
        channel.basicPublish("normalExchange", "unNormalKey", false,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                "test alternate-exchange alternate".getBytes());

        channel.close();
        connection.close();
    }
}
