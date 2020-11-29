package com.example.hazelcastexample.distributed;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author walid.sewaify
 * @since 29-Nov-20
 */
@Service
public class Counter {
    private static final String COUNTER = "counter";
    private final HazelcastInstance cache;

    public Counter(@Qualifier("hazelcastInstance") HazelcastInstance cache) {
        this.cache = cache;
    }

    public long incrementCounter() {
        IAtomicLong counter = cache.getCPSubsystem().getAtomicLong(COUNTER);
        return counter.incrementAndGet();
    }

    public long getCount() {
        IAtomicLong counter = cache.getCPSubsystem().getAtomicLong(COUNTER);
        return counter.get();
    }

    public void reset() {
        IAtomicLong counter = cache.getCPSubsystem().getAtomicLong(COUNTER);
        counter.set(0);
    }
}
