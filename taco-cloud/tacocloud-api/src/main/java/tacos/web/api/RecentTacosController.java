package tacos.web.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;

// adding a custom endpoint besides the ones that Spring Data REST provides
@RepositoryRestController
public class RecentTacosController {

    private TacoRepository tacoRepository;

    public RecentTacosController(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    // ■ The endpoint that handles GET requests for /api/tacos/recent
    @GetMapping(path = "/tacos/recent", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<TacoResource>> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        List<Taco> tacos = tacoRepository.findAll(page).getContent();

        CollectionModel<TacoResource> recentTacoResources = new TacoResourceAssembler().toCollectionModel(tacos);
        recentTacoResources.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecentTacosController.class).recentTacos())
                .withRel("recents"));
        return new ResponseEntity<>(recentTacoResources, HttpStatus.OK);
    }
}

// @RepositoryRestController = the path of all controller's endpoints is prefixed with the value of spring.data.rest.base-path property
// recentTacos() is used instead of recent() method from DesignTacoController

// @RepositoryRestController - is similar with @RestController but it doesn't ensure that values returned from handler methods are automatically written to the body of the response
//      - so you need to annotate the method with @ResponseBody or to return a ResponseEntity that wraps the response data
