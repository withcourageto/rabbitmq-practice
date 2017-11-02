package top.cmoon.rabbitmq.learn.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ChannelUtil {


    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }

    public static void closeChannelAndConn(Channel channel) throws IOException, TimeoutException {
        Connection connection = channel.getConnection();
        channel.close();
        connection.close();
    }

}
