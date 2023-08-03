package tacos.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tacos.Ingredient;

@CrossOrigin(origins="*")
public interface IngredientRepositoryReactive extends ReactiveCrudRepository<Ingredient, String> {

}
// ReactiveCrudRepository is similar with CrudRepository but works with reactive programs


