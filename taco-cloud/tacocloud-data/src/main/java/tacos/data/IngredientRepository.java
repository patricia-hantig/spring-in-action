package tacos.data;


import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tacos.Ingredient;

@CrossOrigin(origins="*")
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}

// CrudRepository declares about a dozen methods for CRUD operations
// CrudRepository it's parameterized - the first param is the entity type the repository will persist & second param is the type of the entity ID property
// with Spring Data JPA - there is no need to write an implementation - when the application starts, Spring Data JPA automatically generates an implementation
// we just need to inject them into the controllers like we did for JDBC-based implementation

// @CrossOrigin = allows clients form any domain to consume the API
//      Angular part of the application - will be running on a separate host/port from the API
//      => The web browser will prevent the Angular client from consuming the API
//      Solution: this restriction can be solved by including CORS(Cross-Origin Resource Sharing) headers in the server application\
//                  - you can apply CORS in Spring with @CrossOrigin annotation

