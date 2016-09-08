package com.chyld.controllers;

import com.chyld.services.RunService;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/runs")
public class RunController {

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


    @RequestMapping( path = "/{sn}/start", method = RequestMethod.POST)
    public void startRun(@PathVariable String sn)
    {

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("Serial", sn);

        String topicName = topicExchange.getName();
        rabbitTemplate.convertAndSend(topicName, "fit.topic.run.start", sn);

    }

    //Start

    //Stop

    @RequestMapping(path = "/{sn}/stop", method = RequestMethod.POST)
    public void stopRun(@PathVariable String sn){

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("Serial", sn);

        String topicName = topicExchange.getName();
        rabbitTemplate.convertAndSend(topicName, "fit.topic.run.stop", sn);
    }
}
