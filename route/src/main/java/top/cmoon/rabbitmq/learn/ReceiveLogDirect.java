package top.cmoon.rabbitmq.learn;

import com.rabbitmq.client.*;
import top.cmoon.rabbitmq.learn.util.ChannelUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogDirect {

    private static final String exchange_name = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtil.getChannel();
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        if (args.length < 1) {
            System.err.println("Usage: ReceiveLogDirect [info] [warning] [error]");
            System.exit(1);
        }

        for (String severity : args) {
            channel.queueBind(queueName, exchange_name, severity);
        }

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[X] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}