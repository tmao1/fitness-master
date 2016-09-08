package com.chyld.messaging;

import com.chyld.entities.Position;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RunMessagingService {

    private com.chyld.services.RunService service;

    @Autowired
    public void setService(com.chyld.services.RunService service) {
        this.service = service;
    }

    @RabbitListener(queues = "fit.queue.run")
    public void receive(Message message, String data) {

        if (message != null && data != null) {
            String key = message.getMessageProperties().getReceivedRoutingKey();
            if (key.equals("fit.topic.run.start")) {
                    service.StartRun(data);
            }else if (key.equals("fit.topic.run.stop")) {
                service.StopRun(data);
            }
        }
    }
}
