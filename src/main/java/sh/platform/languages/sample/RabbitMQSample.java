package sh.platform.languages.sample;

import sh.platform.config.Config;
import sh.platform.config.RabbitMQ;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.function.Supplier;

public class RabbitMQSample implements Supplier<String> {

    @Override
    public String get() {
        StringBuilder logger = new StringBuilder();

        // Create a new config object to ease reading the Platform.sh environment variables.
        // You can alternatively use getenv() yourself.
        Config config = new Config();
        try {
            //Get the credentials to connect to the RabbitMQ service.
            final RabbitMQ kafka = config.getCredential("rabbitmq", RabbitMQ::new);
            final ConnectionFactory connectionFactory = kafka.get();

            //Connect to the RabbitMQ server
            final Connection connection = connectionFactory.createConnection();
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createTemporaryQueue();
            TextMessage textMessage = session.createTextMessage("Platform.sh");
            textMessage.setJMSReplyTo(queue);
            MessageProducer producer = session.createProducer(queue);
            producer.send(textMessage);

            //Receive the message
            MessageConsumer consumer = session.createConsumer(queue);
            TextMessage replyMsg = (TextMessage) consumer.receive();

            logger.append("Message ").append(replyMsg.getText());

            //close connections
            producer.close();
            consumer.close();
            session.close();
            connection.close();
            return logger.toString();
        } catch (Exception exp) {
            throw new RuntimeException("An error when execute RabbitMQ", exp);
        }
    }
}

