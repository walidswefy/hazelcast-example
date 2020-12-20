package com.example.hazelcastexample.distributed;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author walid.sewaify
 * @since 20-Dec-20
 */
@Service
@Profile("publisher")
public class Publisher {

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance instance;

    public void publish(String message) {
        ITopic<String> topic = instance.getTopic("topic");
        topic.publish(message);
    }

    @Scheduled(fixedDelay = 1000)
    public void publishMessage() {
        this.publish(new Date().toString());
    }
}
