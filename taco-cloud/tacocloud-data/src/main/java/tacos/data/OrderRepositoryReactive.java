package tacos.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import reactor.core.publisher.Flux;
import tacos.Order;
import tacos.User;

import java.util.List;

public interface OrderRepositoryReactive extends ReactiveCrudRepository<Order, Long> {
    //Flux<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}

// ReactiveCrudRepository is similar with CrudRepository but works with reactive programs
