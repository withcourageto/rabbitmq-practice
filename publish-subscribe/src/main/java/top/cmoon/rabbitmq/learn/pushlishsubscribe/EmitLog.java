package top.cmoon.rabbitmq.learn.pushlishsubscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

public class EmitLog {

    private static final String EXCHANGE_NAME = "log";

    private static final String DELIMITER = " ";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        String message = getMessage(args);
        channel.basicPublish(EXCHANGE_NAME, "", null
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
