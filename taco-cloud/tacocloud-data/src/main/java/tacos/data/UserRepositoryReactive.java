package tacos.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import reactor.core.publisher.Mono;
import tacos.User;

public interface UserRepositoryReactive extends ReactiveCrudRepository<User, Long> {
    //Mono<User> findByUsername(String username);
    //Mono<User> findByEmail(String email);
}

// ReactiveCrudRepository is similar with CrudRepository but works with reactive programs
