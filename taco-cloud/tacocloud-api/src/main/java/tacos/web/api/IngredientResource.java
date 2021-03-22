package tacos.web.api;

import lombok.Getter;
//import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import tacos.Ingredient;

// ! IMPORTANT: changes starting with Spring HATEOAS version 1.0 : https://spring.io/blog/2019/03/05/spring-hateoas-1-0-m1-released#overhaul
// types were renamed and methods have changed: https://github.com/spring-projects/spring-hateoas/blob/master/etc/migrate-to-1.0.sh

@Relation(value = "ingredient", collectionRelation = "ingredientsList")
//  public class IngredientResource extends ResourceSupport {
public class IngredientResource extends RepresentationModel<IngredientResource> {

    @Getter
    private String name;

    @Getter
    private Ingredient.Type type;

    public IngredientResource(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }

}
// @Relation(value = "ingredient", collectionRelation = "ingredientsList")  = breaks the coupling between the JSON field name and the resource type class names & specifies how Spring HATEOAS should name the field in the resulting JSON
//                                                                          - Here you’ve specified that when a list of IngredientResource objects is used in a Resources object, it should be named ingredientsList

