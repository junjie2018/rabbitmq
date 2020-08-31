package fun.mytoys.rabbitmq.rabbitmq.chap_04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

// 就这样了，不实验了

@SuppressWarnings("Duplicates")
public class T014_RabbitProducer_DelayQueue {
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

        int[] delays = new int[]{5, 10, 30, 60};

        Connection connection = factory.newConnection();

        Map<String, Object> queueArgs = new HashMap<>();
        queueArgs.put("x-dead-letter-exchange", "dlxExchange");

        // 定义一个配置了ttl及死信队列的队列
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("ttlExchange", "direct", true, false, null);
        for (int delay : delays) {
            String queueName = String.format("queue_%ds", delay);
            String bindKey = String.format("%ds", delay);

            queueArgs.put("x-message-ttl", delay * 1000);
            channel.queueDeclare(queueName, true, false, false, queueArgs);
            channel.queueBind(queueName, "ttlExchange", bindKey);

            String dlxExchangeName = String.format("dlx%ds", delay);
            String dlxQeueName = String.format("dlxQueue_%ds", delay);

            channel.exchangeDeclare(dlxExchangeName, "fanout", true, false, null);
            channel.queueDeclare(dlxQeueName, true, false, false, null);
            channel.queueBind(dlxQeueName, dlxExchangeName, bindKey);
        }


        channel.basicPublish("ttlExchange", ROUTING_KEY,
                null,
                "mandatory test".getBytes());
    }
}
