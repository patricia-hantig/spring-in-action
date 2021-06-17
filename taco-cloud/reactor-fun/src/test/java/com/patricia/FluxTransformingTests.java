package com.patricia;

import lombok.Data;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FluxTransformingTests {

    // ====================== filtering data from reactive types ======================

    //  - skipping elements from a Flux

    @Test
    public void skipAFew() {
        Flux<String> skipFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
                .skip(3);

        StepVerifier.create(skipFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete();
    }


    //  - skipping elements from a Flux for a specific time

    @Test
    public void skipAFewSeconds() {
        Flux<String> skipFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));

        StepVerifier.create(skipFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete();
    }


    //  - taking first elements from a Flux

    @Test
    public void take() {
        Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .take(3);

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone")
                .expectNext("Yosemite")
                .expectNext("Grand Canyon")
                .verifyComplete();

        // same StepVerifier as above but more succinct
        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }


    //  - taking first elements from a Flux after a specific time

    @Test
    public void takeAFewSeconds() {
        Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofMillis(3500)); // 3.5 secs

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }


    //  - filter elements from a Flux based on some criteria

    @Test
    public void filter() {
        Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .filter(np -> !np.contains(" "));

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Zion")
                .verifyComplete();
    }


    //  - filter elements that were already received from a Flux

    @Test
    public void distinct() {
        Flux<String> animalFlux = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater")
                .distinct();

        StepVerifier.create(animalFlux)
                .expectNext("dog", "cat", "bird", "anteater")
                .verifyComplete();
    }


    // ====================== mapping reactive data ======================

    //   - transform published elements to some other form/type synchronously

    @Test
    public void map() {
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]);
                });
        StepVerifier.create(playerFlux)
                .expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen"))
                .expectNext(new Player("Steve", "Kerr"))
                .verifyComplete();
    }


    //   - transform published elements to some other form/type asynchronously

    @Test
    public void flatMap() {
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap(n -> Mono.just(n)
                    .map(p -> {
                        String[] split = p.split("\\s");
                        return new Player(split[0], split[1]);
                    })
                    .subscribeOn(Schedulers.parallel())
                );

        List<Player> players = Arrays.asList(
                new Player("Michael", "Jordan"),
                new Player("Scottie", "Pippen"),
                new Player("Steve", "Kerr")
        );

        StepVerifier.create(playerFlux)
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .verifyComplete();
    }
    // flatMap() transforms the incoming String into a Mono of type String
    // map() is applied to the Mono to transform the String to a Player
    // next Mono is calling subscribeOn() to indicate that each subscription should take place in a parallel thread
    // subscribeOn() = specifies how a subscription should be handled concurrently
    // as a concurrency model here we use one static method from Schedulers - parallel()
    // Because the work is being done in parallel, there’s no way to know the order of items emitted in the resulting Flux
    //      - that's why StepVerifier is only able to verify that each item emitted exists in the expected list of Player objects

    //  - Concurrency models for Schedulers: immediate(), single(), newSingle(), elastic(), parallel():
    // immediate() = executes the subscription in the current thread
    // single() = executes the subscription in a single, reusable thread; it reuses the same thread for all callers
    // newSingle() = executes the subscription in a per-call dedicated thread
    // elastic() = executes the subscription in a worker pulled from an elastic poll (New worker threads are created as needed, and idle workers are disposed of (by default, after 60 seconds).)
    // parallel() = executes the subscription in a worker pulled from a fixed-size pool (sized to the number of CPU cores)

    @Data
    private static class Player {
        private final String firstName;
        private final String lastName;
    }


    // ====================== buffering data on a reactive stream ======================

    //  - create a new Flux<List> from Flux<String> where the List has a specified number of elements

    @Test
    public void buffer() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

        Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(Arrays.asList("apple", "orange", "banana"))
                .expectNext(Arrays.asList("kiwi", "strawberry"))
                .verifyComplete();
    }


    //  - create a new Flux<List> from Flux<String> where the List has a specified number of elements - resulting List collections to be processed in parallel

    @Test
    public void bufferAndFlatMap() {
        Flux.just("apple", "orange", "banana", "kiwi", "strawberry")
                .buffer(3)
                .flatMap(x ->
                        Flux.fromIterable(x)
                                .map(y -> y.toUpperCase())
                                .subscribeOn(Schedulers.parallel())
                                .log()
                ).subscribe();
    }

    // - the two buffers are processed in parallel
    // the fruits in first buffer are handled in parallel-1 thread & the fruits in the second buffer are handled in parallel-2 thread
    // Console output:
    //  17:55:33.118 [parallel-1] INFO reactor.Flux.SubscribeOn.1 - onNext(APPLE)
    //  17:55:33.118 [parallel-2] INFO reactor.Flux.SubscribeOn.2 - onNext(KIWI)
    //  17:55:33.118 [parallel-1] INFO reactor.Flux.SubscribeOn.1 - onNext(ORANGE)
    //  17:55:33.118 [parallel-1] INFO reactor.Flux.SubscribeOn.1 - onNext(BANANA)
    //  17:55:33.118 [parallel-2] INFO reactor.Flux.SubscribeOn.2 - onNext(STRAWBERRY)


    //  - collect everything from a Flux into a List - with buffer() method => this produces Flux<List>

    @Test
    public void bufferToList() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

        Flux<List<String>> bufferedFlux = fruitFlux.buffer();

        StepVerifier.create(bufferedFlux)
                .expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }


    //  - collect everything from a Flux into a List - with collectList() method => this produces Mono<List>

    @Test
    public void collectList() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

        Mono<List<String>> fruitListMono = fruitFlux.collectList();

        StepVerifier.create(fruitListMono)
                .expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }


    //  - collect everything from a Flux into a Map

    @Test
    public void collectMap() {
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Map<Character, String>> animalMono = animalFlux.collectMap(a -> a.charAt(0));

        StepVerifier.create(animalMono)
                .expectNextMatches(map -> {
                    return map.size() == 3 &&
                            map.get('a').equals("aardvark") &&
                            map.get('e').equals("eagle") &&
                            map.get('k').equals("kangaroo");
                })
                .verifyComplete();
    }
}
