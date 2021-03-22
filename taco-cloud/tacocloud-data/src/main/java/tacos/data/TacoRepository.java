package tacos.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import tacos.Taco;

public interface TacoRepository extends PagingAndSortingRepository<Taco, Long> {

}

// CrudRepository declares about a dozen methods for CRUD operations
// CrudRepository it's parameterized - the first param is the entity type the repository will persist & second param is the type of the entity ID property
// with Spring Data JPA - there is no need to write an implementation - when the application starts, Spring Data JPA automatically generates an implementation
// we just need to inject them into the controllers like we did for JDBC-based implementation
