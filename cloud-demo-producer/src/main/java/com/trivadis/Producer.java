package com.trivadis;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class Producer {

    Logger LOG = LoggerFactory.getLogger(Producer.class.getName());

    private int counter = 1;

    @Outgoing("work-items")
    @Broadcast
    public Flowable<String> produce() {
        return Flowable.interval(500, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .map(tick -> String.format("Work Item: %d", counter++))
                .doOnNext(LOG::info);
    }
}
