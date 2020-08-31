package fun.mytoys.rabbitmq.rabbitmq.chap_04;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// 我无法得到实验结果

@SuppressWarnings("Duplicates")
public class T001_RabbitProducer_Mandatory {
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

        channel.basicPublish(EXCHANGE_NAME, "", false,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                "mandatory test".getBytes());
        channel.addReturnListener((replyCode, replyText, exchange, routingKey, basicProperties, body) -> {
            String message = new String(body);
            System.out.println("Basic.Return返回的结果是：" + message);
        });

        channel.close();
        connection.close();
    }
}
