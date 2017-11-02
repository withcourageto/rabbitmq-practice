package top.cmoon.rabbitmq.learn.topic;

import com.rabbitmq.client.*;
import top.cmoon.rabbitmq.learn.util.ChannelUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsTopic {

    private static final String exchange_name = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        if (args.length < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        Channel channel = ChannelUtil.getChannel();
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);
//        String queueName = channel.queueDeclare().getQueue();

        String[] queueNames = queues(channel, 2);
        for (String bindingKey : args) {
            for (String queueName : queueNames) {
                channel.queueBind(queueName, exchange_name, bindingKey);
            }
        }

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[X] " + consumerTag);
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "', channel is :" + this.getChannel());
            }
        };
        for (String queueName : queueNames)
            channel.basicConsume(queueName, true, consumer);
    }

    private static String[] queues(Channel channel, int length) throws IOException {
        if (length < 1) {
            throw new IllegalArgumentException("value of param length must greater than or equal 1, actual is : " + length);
        }

        String[] queueNames = new String[length];
        for (int i = 0; i < length; i++) {
            queueNames[i] = channel.queueDeclare().getQueue();
        }

        return queueNames;
    }

}
