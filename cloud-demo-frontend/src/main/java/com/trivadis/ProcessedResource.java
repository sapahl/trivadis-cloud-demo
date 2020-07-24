package com.trivadis;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/processed")
public class ProcessedResource {

    Logger LOG = LoggerFactory.getLogger(ProcessedResource.class.getName());

    @Inject
    @Liveness
    FrontendHealthCheck healthCheck;

    private final BlockingQueue<String> cache = new LinkedBlockingQueue<>();
    private final AtomicInteger counter = new AtomicInteger(1);

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public Publisher<String> stream() {
        return Flowable.create(emitter -> {
            int request = counter.getAndIncrement();
            String item;
            healthCheck.setStatus(HealthCheckResponse.up("Waiting for work item"));
            while (request + 1 == counter.get() && (item = cache.poll(1, TimeUnit.MINUTES)) != null) {
                emitter.onNext(item);
                LOG.info("Stream for request {} returns: {}", request, item);
                healthCheck.setStatus(HealthCheckResponse.up("Receiving processed work items: " + item));
            }
            emitter.onComplete();
            healthCheck.setStatus(HealthCheckResponse.up("Processing timed out. Please reload"));
        }, BackpressureStrategy.LATEST);
    }

    @Incoming("work-items-processed")
    public CompletionStage<Void> receiveProcessedWorkItem(Message<String> processedWorkItem) {
        cache.offer(processedWorkItem.getPayload());
        return processedWorkItem.ack();
    }
}