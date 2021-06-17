package com.patricia;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FluxLoggingTests {

    //  - logging examples

    @Test
    public void logSimple() {
        Flux<String> colors = Flux.just("white", "yellow", "orange", "green", "purple", "blue")
                .log();
        colors.subscribe();
    }

    @Test
    public void logMapping() {
        Flux<String> colors = Flux.just("white", "yellow", "orange", "green", "purple", "blue")
                .map(cb -> cb.toUpperCase())
                .log();
        colors.subscribe();
    }

    @Test
    public void logFlatMapping() throws Exception {
        Flux<String> colors = Flux.just("white", "yellow", "orange", "green", "purple", "blue")
                .flatMap(cb -> Mono.just(cb)
                        .map(c -> c.toUpperCase())
                        .log()
                        .subscribeOn(Schedulers.parallel())
                )
                ;
        colors.subscribe();

        Thread.sleep(3000L);
    }
}
