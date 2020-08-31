package fun.mytoys.rabbitmq.rabbitmq.chap_03;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("Duplicates")
public class T005_RabbitConsumer_ShutdownListener {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routekey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.30.174";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = new Address[]{new Address(IP_ADDRESS, PORT)};

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("123456");

        // 连接方式与生产者不同（只是写法不同，已验证）
        Connection connection = factory.newConnection(addresses);
        connection.addShutdownListener((cause) -> {
            if (cause.isHardError()) {
                Connection conn = (Connection) cause.getReference();
                if (!cause.isInitiatedByApplication()) {
                    Method reason = cause.getReason();
                }
            } else {
                Channel ch = (Channel) cause.getReference();
                // to do something
            }
            System.out.println(cause.getReason());
        });

        Channel channel = connection.createChannel();
        GetResponse response = channel.basicGet(QUEUE_NAME, false);
        System.out.println(new String(response.getBody()));
        channel.basicAck(response.getEnvelope().getDeliveryTag(), false);

        channel.close();
        connection.close();
    }
}
