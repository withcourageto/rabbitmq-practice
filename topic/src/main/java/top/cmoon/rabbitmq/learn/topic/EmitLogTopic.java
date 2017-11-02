package top.cmoon.rabbitmq.learn.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import top.cmoon.rabbitmq.learn.util.ArgUtil;
import top.cmoon.rabbitmq.learn.util.ChannelUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {

    private static final String exchange_name = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtil.getChannel();

        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);

        String routingKey = ArgUtil.getRouting(args).orElse("info");
        String message = ArgUtil.getMessage(args, 1);

        channel.basicPublish(exchange_name, routingKey, null
                , message.getBytes());

        System.out.println("[X] Sent '" + routingKey + "':'" + message + "'");
        ChannelUtil.closeChannelAndConn(channel);
    }
}
