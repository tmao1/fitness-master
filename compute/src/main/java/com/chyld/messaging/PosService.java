package com.chyld.messaging;

import com.chyld.entities.Position;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PosService {

    private com.chyld.services.PositionService service;

    @Autowired
    public void setService(com.chyld.services.PositionService service) {
        this.service = service;
    }

    @RabbitListener(queues = "fit.queue.pos")
    public void receive(Message message, HashMap<String, Object> data){

        if( message != null && data != null) {
            String key = message.getMessageProperties().getReceivedRoutingKey();
            String serial = data.get("Serial").toString();
            Position position = (Position) data.get("position");

            service.savePosition(position, serial);
        }

    }
}
