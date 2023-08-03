package tacos.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import reactor.core.publisher.Mono;
import tacos.PaymentMethod;

public interface PaymentRepositoryReactive extends ReactiveCrudRepository<PaymentMethod, Long> {
    //Mono<PaymentMethod> findByUserId(Long userId);
}

// ReactiveCrudRepository is similar with CrudRepository but works with reactive programs
