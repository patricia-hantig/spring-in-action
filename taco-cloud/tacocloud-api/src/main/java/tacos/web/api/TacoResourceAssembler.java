package tacos.web.api;

//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import tacos.Taco;

// ! IMPORTANT: changes starting with Spring HATEOAS version 1.0 : https://spring.io/blog/2019/03/05/spring-hateoas-1-0-m1-released#overhaul
// types were renamed and methods have changed: https://github.com/spring-projects/spring-hateoas/blob/master/etc/migrate-to-1.0.sh

//  public class TacoResourceAssembler extends ResourceAssemblerSupport<Taco, TacoResource> {
public class TacoResourceAssembler extends RepresentationModelAssemblerSupport<Taco, TacoResource> {

    public TacoResourceAssembler() {
        super(DesignTacoController.class, TacoResource.class);
    }

//    @Override
//    protected TacoResource instantiateResource(Taco taco) {
//        return new TacoResource(taco);
//    }

//    @Override
//    public TacoResource toResource(Taco taco) {
//        return createResourceWithId(taco.getId(), taco);
//    }

    @Override
    public TacoResource toModel(Taco taco) {
        return createModelWithId(taco.getId(), taco);
    }

    @Override
    protected TacoResource instantiateModel(Taco taco) {
        return new TacoResource(taco);
    }

}
// this class is needed for conversion from Taco to TacoResource
// TacoResourceAssembler has a constructor that says which controller (DesignTacoController) will use to determine the path for any URLs in the link it creates when creating TacoResource

// instantiateResource()    = is used to instantiate a TacoResource given a Taco
//                          - it would be optional - if TacoResource has a default constructor
// toResource() = is used to create a TacoResource object from a Taco & to automatically give it a self link with the uRL derived from the Taco object's id property
//              - is the only mandatory method which needs to be overridden when extending ResourceAssemblerSupport

// - instantiateResource()  = instantiates a Resource object
// - toResource()           = creates a Resource object & populates it with links
//      -> under the covers: toResource() will call instantiateResource()