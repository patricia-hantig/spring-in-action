package com.patricia.security;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//import reactor.core.publisher.Mono;

public interface UserRepositoryReactive extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByUsername(String username);
}

// ReactiveCrudRepository is similar with CrudRepository but works with reactive programs
