package com.patricia;

import reactor.core.publisher.Mono;

// imperative programming vs reactive programming
public class FirstExample {

    public static void main(String[] args) {
        // imperative model
        String name = "Craig";
        String capitalName = name.toUpperCase();
        String greeting = "Hello, " + capitalName + "! (imperative model)";
        System.out.println(greeting);

        // reactive model
        Mono.just("Craig")
                .map(n -> n.toUpperCase())
                .map(cn -> "Hello, " + cn + "! (reactive model)")
                .subscribe(System.out::println);
    }
}
// we want to take a person's name, change its letters to uppercase & use it to create a greeting message
