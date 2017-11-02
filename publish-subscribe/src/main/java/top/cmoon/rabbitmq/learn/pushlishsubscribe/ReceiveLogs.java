package top.cmoon.rabbitmq.learn.pushlishsubscribe;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {
    private final static String EXCHANGE_NAME = "log";

    public static void main(String[] argv)
            throws IOException,
            InterruptedException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare();

        String queueName = declareOk.getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new LogConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
    }


    private static class LogConsumer extends DefaultConsumer {

        /**
         * Constructs a new instance and records its association to the passed-in channel.
         *
         * @param channel the channel to which this consumer is attached
         */
        public LogConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope,
                                   AMQP.BasicProperties properties, byte[] body)
                throws IOException {
            String message = new String(body, "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }


    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
