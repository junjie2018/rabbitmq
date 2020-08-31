package fun.mytoys.rabbitmq.rabbitmq.chap_04;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// 我无法得到实验结果

@SuppressWarnings("Duplicates")
public class T032_RabbitProducer_PublisherConfirm2 {
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

        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Ack, SeqNo: " + deliveryTag + ", Mul: " + multiple);
                if (multiple) {
                    // confirmSet.headSet(deliveryTag - 1).clear();
                } else {
                    // confirmSet.remove(deliveryTag);
                }
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    // confirmSet.headSet(deliveryTag - 1).clear();
                } else {
                    // confirmSet.remove(deliveryTag - 1).clear();
                }

                // 添加消息重发机制
            }
        });

        while (true) {
            long nextSeqNo = channel.getNextPublishSeqNo();
            // 发送逻辑
            // 添加序列
            // confirm.add(nextSeqNo);
        }

        channel.close();
        connection.close();
    }
}
