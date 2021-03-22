package tacos.web.api;

//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import tacos.Ingredient;

// ! IMPORTANT: changes starting with Spring HATEOAS version 1.0 : https://spring.io/blog/2019/03/05/spring-hateoas-1-0-m1-released#overhaul
// types were renamed and methods have changed: https://github.com/spring-projects/spring-hateoas/blob/master/etc/migrate-to-1.0.sh

//  public class IngredientResourceAssembler extends ResourceAssemblerSupport<Ingredient, IngredientResource> {
public class IngredientResourceAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientResource> {

    public IngredientResourceAssembler() {
        super(IngredientController.class, IngredientResource.class);
    }

//    @Override
//    public IngredientResource toResource(Ingredient ingredient) {
//        return createResourceWithId(ingredient.getId(), ingredient);
//    }
//
//    @Override
//    protected IngredientResource instantiateResource(Ingredient ingredient) {
//        return new IngredientResource(ingredient);
//    }

    @Override
    public IngredientResource toModel(Ingredient ingredient) {
        return createModelWithId(ingredient.getId(), ingredient);
    }

    @Override
    protected IngredientResource instantiateModel(Ingredient ingredient) {
        return new IngredientResource(ingredient);
    }
}
