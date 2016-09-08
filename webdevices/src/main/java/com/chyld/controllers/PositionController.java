package com.chyld.controllers;

import com.chyld.entities.Position;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/positions")
public class PositionController {

    private RabbitTemplate rabbitTemplate;
    private TopicExchange topicExchange;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setTopicExchange(TopicExchange topicExchange) {
        this.topicExchange = topicExchange;
    }

    @RequestMapping(path = "/{sn}", method = RequestMethod.POST)
    public void addPosition(@PathVariable String sn, @RequestBody Position position) {

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("Serial", sn);
        hm.put("position", position);




        String topicName = topicExchange.getName();
        rabbitTemplate.convertAndSend(topicName, "fit.topic.pos", hm);

    }
}
