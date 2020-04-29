package com.trivadis;

import io.reactivex.Flowable;
import io.reactivex.processors.AsyncProcessor;
import kafka.log.Log;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.resteasy.plugins.providers.CompletionStageProvider;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class Processor {

    Logger LOG = LoggerFactory.getLogger(Processor.class.getName());
    private Random random = new Random();

    @Incoming("work-items")
    @Outgoing("work-items-processed")
    public Flowable<String> createPublisher(Flowable<String> flow) {
        return flow.map(item -> String.format("%s processed %s", getHostName(), item))
                .doOnNext(LOG::info)
                .delay(500 + random.nextInt(500), TimeUnit.MILLISECONDS);
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown Pod";
        }
    }
}
