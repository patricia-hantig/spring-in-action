package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
//import org.springframework.hateoas.Resource;
//import org.springframework.hateoas.Resources;
//import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/design", produces = "application/json")            // <1> handles requests for /design
@CrossOrigin(origins = "*")                                                 // <2> allows cross-origin requests
public class DesignTacoController {

    private TacoRepository tacoRepository;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    public DesignTacoController(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }
    // here all the Repositories are injected into DesignTacoController and they can now be used in the next methods


    // ■ The endpoint that handles GET requests for /design/recent  => responds with a list of recently designed tacos
    @GetMapping("/recent")
    public Iterable<Taco> recentTacos() {               //<3>
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(pageRequest).getContent();
    }
    // this method fetches and returns recent taco designs
    // PageRequest object specifies that you only want the first (0th) page of 12 results, sorted in descending order by the taco’s creation date


    // ■ The endpoint that handles GET requests for /design/{id}  => responds with a Taco object
    @GetMapping("/{id}")
    public Taco tacoById(@PathVariable("id") Long id) {
        Optional<Taco> optionalTaco = tacoRepository.findById(id);
        if (optionalTaco.isPresent()) {
            return optionalTaco.get();
        }
        return null;
    }
    // this method returns a Taco Object
    // - if the id matches a Taco           => you will get the actual Taco obj
    // - if the id DOES NOT match a Taco    => you return null => NOT IDEAL
    //                                          - the client receives a response with an empty body & an HTTP status code of 200(OK)
    //                                          => the status indicates that everything is fine !!!! WRONG
    //                                          - it should return a response with an HTTP 404(NOT FOUND)

    /*===================== commented because we use the method tacoById() =====================*/
    // alternative method for tacoById() which solves the above problem
    // if the id DOES NOT match a Taco - it returns a response with an HTTP 404(NOT FOUND)
    /*@GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById2(@PathVariable("id") Long id) {
        Optional<Taco> optionalTaco = tacoRepository.findById(id);
        if (optionalTaco.isPresent()) {
            return new ResponseEntity<>(optionalTaco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }*/


    // ■ The endpoint that handles POST requests for /design  => saves a taco (how to create a taco)
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepository.save(taco);
    }


    /*===================== commented because we use the method recentTacosHyperlink2() =====================*/
    /*@GetMapping("/recent")
    // ! IMPORTANT: changes starting with Spring HATEOAS version 1.0 : https://spring.io/blog/2019/03/05/spring-hateoas-1-0-m1-released#overhaul
    // types were renamed and methods have changed: https://github.com/spring-projects/spring-hateoas/blob/master/etc/migrate-to-1.0.sh
//    public Resources<Resource<Taco>> recentTacosHyperlink() {
    public CollectionModel<EntityModel<Taco>> recentTacosHyperlink() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepository.findAll(pageRequest).getContent();
        CollectionModel<EntityModel<Taco>> recentResources = CollectionModel.wrap(tacos);

        *//*recentResources.add(new Link("http://localhost:8080/design/recent", "recents"));*//*
        // here we no longer return the list of tacos directly
        // we use Resources.wrap() - to wrap the list of tacos as an instance of Resources<Resource<Taco>>
        // we also add a link with name & URL to the Resources object before returning - name= "recents" and URL = "http://localhost:8080/design/recent"

        // rewrite the hardcoded Link creation
        *//*recentResources.add(ControllerLinkBuilder.linkTo(DesignTacoController.class)        // URL = /design
                .slash("recent")                                                    // URL = /design/recent
                .withRel("recents"));                                                       // name = recents
        *//*
        // or:

//        recentResources.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(DesignTacoController.class).recentTacosHyperlink())
        recentResources.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DesignTacoController.class).recentTacosHyperlink())
                .withRel("recents"));

        return recentResources;
    }*/
    // so in the JSON response of the API request we will have something like this:
    /*"_links": {
        "recents": {
            "href": "http://localhost:8080/design/recent"
        }
    }*/

    // the hardcoded URL = bad idea !
    // instead of the hardcoded URL => Spring HATEOAS offers help in the form of link builders
    // -> the most useful of Spring HATEOAS link builders is: ControllerLinkBuilder

    /*===================== commented because we use the method recentTacosHyperlink2() =====================*/
    /*===================== this method uses TacoResource and TacoResourceAssembler =====================*/
    /*@GetMapping("/recent")
//    public Resources<TacoResource> recentTacosHyperlink2() {
    public CollectionModel<TacoResource> recentTacosHyperlink2() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        List<Taco> tacos = tacoRepository.findAll(pageRequest).getContent();

//        List<TacoResource> tacoResources =  new TacoResourceAssembler().toResources(tacos);
        CollectionModel<TacoResource> recentTacoResources = new TacoResourceAssembler().toCollectionModel(tacos);
//        recentTacoResources.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(DesignTacoController.class).recentTacosHyperlink2())
        recentTacoResources.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DesignTacoController.class).recentTacosHyperlink2())
                .withRel("recents"));
        return recentTacoResources;
    }*/
    // after fetching the tacos from repository (tacos list) you pass the list of Taco objects to the toResources() method
    // toResources() goes through all the Taco objects and creates a list of TacoResource objects
    // using the list of TacoResource objects -> we create a Resources<TacoResource> objects & populate it with the recents links like in recentTacosHyperlink() method
    // now a GET request to /design/recent -> will produce a list of tacos - each with a self link & a recents link on the list itself

}

// ■■■ Annotations:

// @RequestMapping = at class level - specifies the kind of request that this controller handles : here will handle requests whose path begins with /design
// ■■ Handling a GET request:
//      @RequestMapping is connected with @GetMapping annotation before showDesignForm() method
//      @GetMapping paired with class-level @RequestMapping = specifies that when an HTTP GET request is received for /design -> showDesignForm() is called to handle the request
//          - @GetMapping was introduced in Spring 4.3, before you should use a method-level @RequestMapping annotation: @RequestMapping(method=RequestMethod.GET)
//      ■ Spring MVC request-mapping annotations:
//              Annotation      |         Description
//      -------------------------------------------------------------
//          @RequestMapping     |   general-purpose request handling
//          @GetMapping         |   handles HTTP GET requests
//          @PostMapping        |   handles HTTP POST requests
//          @PutMapping         |   handles HTTP PUT requests
//          @DeleteMapping      |   handles HTTP DELETE requests
//          @PatchMapping       |   handles HTTP PATCH requests
// @RequestMapping(path = "/design", produces = "application/json") - produces = any of the handler methods will only handle requests if the request's Accept header includes "application/json"
//                                                                  - API -> will only produce json results + allows another controller to handle requests with the same path if those requests don't require JSON output

// @RestController  - marks the class for discovery component scanning (like @Controller or @Service)
//                  = tells Spring that all handler methods in the controller should have their return value written directly to the body of the response

// @CrossOrigin = allows clients form any domain to consume the API
//      Angular part of the application - will be running on a separate host/port from the API
//      => The web browser will prevent the Angular client from consuming the API
//      Solution: this restriction can be solved by including CORS(Cross-Origin Resource Sharing) headers in the server application\
//                  - you can apply CORS in Spring with @CrossOrigin annotation

// @PathVariable("id") = the actual value in the request is given to the id param which is mapped to the {id} placeholder by @PathVariable annotation

// @PostMapping(consumes = "application/json")  = specify that this method is responsible for POST requests for '/design'
//                                              - consumes says that the method will only handle request with Content-type = application/json

// @RequestBody Taco taco   = the body of the request should be converted to a Taco object & bound to the parameter
//                          - IT'S IMPORTANT - without it Spring MVC assumes that you want request parameters to be bound to the Taco object
//                          - ensures that JSON in the request body is bound to the Taco object

// @ResponseStatus(HttpStatus.CREATED) - so that the response will have the HTTP status of 201 - CREATED = tells the client that a resource was created (in case of a successful request)
