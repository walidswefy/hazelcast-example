package com.example.hazelcastexample.distributed;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author walid.sewaify
 * @since 20-Dec-20
 */
@Service
@Profile("subscriber")
@Slf4j
public class Subscriber {

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance instance;

    @PostConstruct
    public void subscribe() {
        ITopic<String> topic = instance.getTopic("topic");
        topic.addMessageListener(message -> log.info("Received: " + message.getMessageObject()));
    }
}
