package com.patricia;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FluxCreationTests {

    // ====================== creating Flux from objects ======================

    @Test
    public void createAFlux_just() {
        Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");

        fruitFlux.subscribe(f -> System.out.println("Here's some fruit: " + f));

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }


    // ====================== creating Flux from collections ======================

    //  - from array

    @Test
    public void createAFlux_fromArray() {
        String[] fruits = new String[] { "Apple", "Orange", "Grape", "Banana", "Strawberry" };

        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    //  - from a List, Set or any implementation of Iterable

    @Test
    public void createAFlux_fromIterable() {
        List<String> fruitsList = new ArrayList<>();
        fruitsList.add("Apple");
        fruitsList.add("Orange");
        fruitsList.add("Grape");
        fruitsList.add("Banana");
        fruitsList.add("Strawberry");

        Flux<String> fruitFlux = Flux.fromIterable(fruitsList);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    //  - from a Java Stream

    @Test
    public void createAFlux_fromStream() {

        Stream<String> fruitStream = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        Flux<String> fruitFlux = Flux.fromStream(fruitStream);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    // ====================== generating Flux Data ======================

    //  - with range()

    @Test
    public void createAFlux_range() {
        Flux<Integer> flux = Flux.range(1, 5);

        StepVerifier.create(flux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    //  - with interval()

    @Test
    public void createAFlux_interval() {
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1)).take(5);

        StepVerifier.create(flux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }
}
