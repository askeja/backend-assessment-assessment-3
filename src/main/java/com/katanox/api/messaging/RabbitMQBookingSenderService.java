package com.katanox.api.messaging;

import com.katanox.api.dto.BookingDTO;
import com.katanox.api.service.impl.LogWriterService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMQBookingSenderService {

    @Autowired
    LogWriterService logWriterService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${katanox.rabbitmq.exchange}")
    private String exchange;

    @Value("${katanox.rabbitmq.routingkey}")
    private String routingkey;

    @Value("${katanox.rabbitmq.queue}")
    private String queue;

    public String ObjectRabbitMQSender(BookingDTO booking) {
        logWriterService.logStringToConsoleOutput("Sending booking message for booking with id :" + booking.bookingId);

        UUID correlationId = UUID.randomUUID();
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties
                    = message.getMessageProperties();
            messageProperties.setReplyTo(queue);
            messageProperties.setCorrelationId(correlationId.toString());
            return message;
        };
        rabbitTemplate.convertAndSend(exchange, routingkey, booking, messagePostProcessor);
        return correlationId.toString();
    }
}
