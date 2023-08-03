package tacos.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import tacos.Taco;

public interface TacoRepositoryReactive extends ReactiveCrudRepository<Taco, Long> {

}

// ReactiveCrudRepository is similar with CrudRepository but works with reactive programs
