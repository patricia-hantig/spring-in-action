package tacos.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Taco;
import tacos.data.TacoRepositoryReactive;

// reactive controller for DesignTacoController
@RestController
@RequestMapping(path = "/v2/design", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoControllerReactive {

    private TacoRepositoryReactive tacoRepositoryReactive;

    public DesignTacoControllerReactive(TacoRepositoryReactive tacoRepositoryReactive) {
        this.tacoRepositoryReactive = tacoRepositoryReactive;
    }

    @GetMapping("/recent")
    public Flux<Taco> recentTacos() {
        return tacoRepositoryReactive.findAll().take(12);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Taco> postTaco(@RequestBody Taco taco) {
        return tacoRepositoryReactive.save(taco);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable("id") Long id) {
        return tacoRepositoryReactive.findById(id);
    }
}
