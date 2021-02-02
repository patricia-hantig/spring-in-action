package com.patricia.tacocloud.data;

import com.patricia.tacocloud.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository extends CrudRepository<Taco, Long> {

}

// CrudRepository declares about a dozen methods for CRUD operations
// CrudRepository it's parameterized - the first param is the entity type the repository will persist & second param is the type of the entity ID property
// with Spring Data JPA - there is no need to write an implementation - when the application starts, Spring Data JPA automatically generates an implementation
// we just need to inject them into the controllers like we did for JDBC-based implementation
