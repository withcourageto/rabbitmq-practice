package top.cmoon.rabbitmq.learn;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import top.cmoon.rabbitmq.learn.util.ArgUtil;
import top.cmoon.rabbitmq.learn.util.ChannelUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtil.getChannel();

        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        String message = ArgUtil.getMessage(args);
        channel.basicPublish("", QUEUE_NAME, null
                , message.getBytes());

        System.out.println("[X] Sent '" + message + "'");
        ChannelUtil.closeChannelAndConn(channel);
    }
}
