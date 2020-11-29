package com.example.hazelcastexample;

import com.example.hazelcastexample.distributed.Counter;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class HazelcastExampleApplication implements CommandLineRunner {
    @Autowired
    private Counter counter;

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance cache;

    @Value("#{T(java.lang.Math).random() * 1000.0}")
    private long instanceId;

    public static void main(String[] args) {
        SpringApplication.run(HazelcastExampleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        IMap<String, List<Long>> runningInstances = cache.getMap("runningInstances");
        String key = "instances";
        runningInstances.putIfAbsent(key, new ArrayList<>());
        List<Long> applications = runningInstances.get(key);
        applications.add(instanceId);
        runningInstances.put(key, applications);
        //print internal identifiers
        System.out.println("Running instances are " + applications);

        // print hazelcast members
        cache.getCluster().getMembers().stream().map(Member::getUuid).forEach(System.out::println);
    }

    @Scheduled(fixedDelay = 500)
    public void printCounter() {
        int winningCount = 200;
        if (counter.getCount() > winningCount)
            return;
        long counterValue = counter.incrementCounter();
        if (counterValue == winningCount) {
            System.out.println("I am a winner: " + instanceId);
        }
    }
}
