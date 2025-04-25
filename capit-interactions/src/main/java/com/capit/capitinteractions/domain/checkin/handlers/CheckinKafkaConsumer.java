package com.capit.capitinteractions.domain.checkin.handlers;

import com.capit.capitinteractions.domain.checkin.events.CheckinKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckinKafkaConsumer {

    private final KafkaReceiver<String, CheckinKafkaEvent> kafkaReceiver;
    private final Sinks.Many<CheckinKafkaEvent> checkinSink;
    private final String instanceId;

    @PostConstruct
    public void consume() {
        kafkaReceiver.receive()
            .flatMap(record -> {
                CheckinKafkaEvent event = record.value();
                // Fan-out: skip self-published events
                if (!event.getInstanceId().equals(instanceId)) {
                    Sinks.EmitResult result = checkinSink.tryEmitNext(event);
                    if (result.isFailure()) {
                        log.warn("Fan-out emit failed for event {} : {}", event, result);
                    }
                }
                record.receiverOffset().acknowledge();
                return Mono.empty();
            })
            .doOnError(err -> log.error("Error in Kafka consumer stream", err))
            .subscribe();
    }
}
