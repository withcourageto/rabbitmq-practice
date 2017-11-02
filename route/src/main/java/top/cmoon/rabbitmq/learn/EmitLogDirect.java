package top.cmoon.rabbitmq.learn;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import top.cmoon.rabbitmq.learn.util.ArgUtil;
import top.cmoon.rabbitmq.learn.util.ChannelUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {

    private static final String exchange_name = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = ChannelUtil.getChannel();

        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT);

        String severity = ArgUtil.getSeverity(args);
        String message = ArgUtil.getMessage(args, 1);

        channel.basicPublish(exchange_name, severity, null, message.getBytes());
        System.out.println("[X] Sent '" + severity + "':'" + message + "'");

        ChannelUtil.closeChannelAndConn(channel);
    }
}
