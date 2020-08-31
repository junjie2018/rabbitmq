package fun.mytoys.rabbitmq.rabbitmq.chap_03;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("Duplicates")
public class T004_RabbitProducer_Exchanges {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routekey_demo";
    private static final String BINDING_KEY = "routekey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.30.174";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare("source", "direct", false, true, null);
        channel.exchangeDeclare("destination", "fanout", false, true, null);
        channel.exchangeBind("destination", "source", "exKey");
        channel.queueDeclare("queue", false, false, true, null);
        channel.queueBind("queue", "destination", "");
        channel.basicPublish("source", "exKey", null, "exToExDemo".getBytes());

        channel.close();
        connection.close();
    }
}
