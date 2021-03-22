package tacos.web.api;

import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.server.core.Relation;
import tacos.Taco;

import java.util.Date;
import java.util.List;

// ! IMPORTANT: changes starting with Spring HATEOAS version 1.0 : https://spring.io/blog/2019/03/05/spring-hateoas-1-0-m1-released#overhaul
// types were renamed and methods have changed: https://github.com/spring-projects/spring-hateoas/blob/master/etc/migrate-to-1.0.sh

@Relation(value = "taco", collectionRelation = "tacos")
//  public class TacoResource extends ResourceSupport
public class TacoResource extends RepresentationModel<TacoResource> {

    @Getter
    private final String name;

    @Getter
    private final Date createdAt;

    /*@Getter
    private final List<Ingredient> ingredients;

    public TacoResource(Taco taco) {
        this.name = taco.getName();
        this.createdAt = taco.getCreatedAt();
        this.ingredients = taco.getIngredients();
    }*/

    // we change TacoResource to use IngredientResource objects instead of Ingredient objects
    private static final IngredientResourceAssembler ingredientAssembler = new IngredientResourceAssembler();

    @Getter
//    private final List<IngredientResource> ingredients;
    private final CollectionModel<IngredientResource> ingredients;

    public TacoResource(Taco taco) {
        this.name = taco.getName();
        this.createdAt = taco.getCreatedAt();
//      this.ingredients = ingredientAssembler.toResources(taco.getIngredients());
        this.ingredients = ingredientAssembler.toCollectionModel(taco.getIngredients());
    }
    // now the recent tacos list has hyperlinks:
    //          - for itself (recents link)
    //          - for all its taco entities
    //          - for all the ingredients of those tacos

}

// this class is a utility class that converts Taco objects to a new TacoResource object
// TacoResource will look a lot like Taco + will carry links

// TacoResource extends ResourceSupport = to inherit a list of Link object and methods to manage the list of links
// TacoResource does not have the id property - > because we don't need database-specific IDs in the API
// the resource's self link = will be the identifier for the resource
// the constructor - does the conversion from Taco to TacoResource

// @Relation(value = "taco", collectionRelation = "tacos")  = breaks the coupling between the JSON field name and the resource type class names & specifies how Spring HATEOAS should name the field in the resulting JSON
//                                                          - Here you’ve specified that when a list of TacoResource objects is used in a Resources object, it should be named tacos
