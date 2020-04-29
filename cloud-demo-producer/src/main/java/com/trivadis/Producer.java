package com.trivadis;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
                .map(tick -> String.format("Work Item: %d", counter++ ) )
                .doOnNext(LOG::info);
    }
}
