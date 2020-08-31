package fun.mytoys.rabbitmq.rabbitmq.chap_03;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("Duplicates")
public class T003_RabbitConsumer_Push {
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

        Channel channel = connection.createChannel();
        // 设置客户端最多接收未被ack的消息的个数
        channel.basicQos(64);
        channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("recv message: " + new String(body));

                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag, false);
            }
        });

        TimeUnit.SECONDS.sleep(5);

        channel.close();
        connection.close();
    }
}
