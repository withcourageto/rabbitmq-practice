package top.cmoon.rabbitmq.learn;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

public class Send {

    private static final String QUEUE_NAME = "hello";
    private static final String DELIMITER = " ";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);


        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = getMessage(args);
        channel.basicPublish("", QUEUE_NAME, null
                , message.getBytes());




        System.out.println("[X] Sent '" + message + "'");
        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1) {
            return "hello world";
        }
        return joinStrings(strings, DELIMITER);
    }

    private static String joinStrings(String[] strings, String delimiter) {
        if (strings.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(strings[0]);

        Stream.of(strings)
                .skip(1)
                .forEach(s -> result.append(delimiter).append(s));

        return result.toString();
    }

}
