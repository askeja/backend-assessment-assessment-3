package com.katanox.api.messaging;

import com.katanox.api.dto.BookingDTO;
import com.katanox.api.repository.BookingRepository;
import com.katanox.api.service.impl.LogWriterService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "${katanox.rabbitmq.queue}", id = "listener")
public class RabbitMQBookingReceiverService {

    @Autowired
    LogWriterService logWriterService;

    @Autowired
    private final BookingRepository bookingRepository;
    public RabbitMQBookingReceiverService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @RabbitHandler
    public void receiver(BookingDTO booking, Message message) {
        // update booking with correlation Id to show if it is consumed or not
        String correlationId =  message.getMessageProperties().getCorrelationId();
        bookingRepository.updateBooking(booking.bookingId, correlationId);
        logWriterService.logStringToConsoleOutput(String.format("Consuming booking message for booking with id = %s with message correlation id = %s" , booking.bookingId,  message.getMessageProperties().getCorrelationId()));
    }
}
