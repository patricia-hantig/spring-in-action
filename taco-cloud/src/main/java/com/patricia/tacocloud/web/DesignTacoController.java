package com.patricia.tacocloud.web;

import com.patricia.tacocloud.Ingredient;
import com.patricia.tacocloud.Ingredient.Type;
import com.patricia.tacocloud.Order;
import com.patricia.tacocloud.Taco;

import com.patricia.tacocloud.data.IngredientRepository;
import com.patricia.tacocloud.data.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    // chapter 2: showDesignForm() method
    /*@GetMapping
    public String showDesignForm(Model model) {

        // Model = an object that passes data between a controller and the view that have to render data

        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );
        // here we construct a list of Ingredient objects - the list is hardcoded for now (we will change it to pull the list from taco ingredients from a database)

        Type[] types = Ingredient.Type.values();
        // then a list of ingredient types is created

        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type)); // the list is filtered by ingredient type
        }
        // a list of ingredient types is added as an attribute to the Model object (that's passed into showDesignForm())

        model.addAttribute("taco", new Taco());
        // besides the other attributes added to model, we also add an attribute: name = "design" with value = new Taco()

        return "design";
        // returns "design" - which is the logical name of the view that will be used to render the model to the browser

        // !!! Spring does this for us:
        //          -> The data that's placed in Model attributes is copied into the servlet response attributes - where the view can find them
    }*/

    private final IngredientRepository ingredientRepository;

    private TacoRepository designRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository designRepository) {
        this.ingredientRepository = ingredientRepository;
        this.designRepository = designRepository;
    }
    // here all the Repositories are injected into DesignTacoController and they can now be used in the next methods

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        // model.addAttribute("taco", new Taco());
        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    // When we click on the submit button we get: HTTP 405 Error: Request Method "POST" Not Supported
    // Why? - because method attribute (in design.html) is set to POST & <form> doesn't have an action attribute => when the form is submitted:
    //      -> the browser gather up all the data in the form & send it to the server in an HTTP POST request to the same path for which a GET request displayed the form - the /design path
    // Solution: - We need a controller handler method to choose what happens when we receive that POST request
    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        // - if there are any validation errors => those details are captures within an Errors object
        //      - if there are any Errors -> the method doesn't process the Taco and returns "design" which mean the form is redisplayed

        // Save the taco design...
        // We'll do this in chapter 3
        log.info("Processing taco: " + design);

        Taco saved = designRepository.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }
    // when the form is submitted: the fields in the form are bound to properties of a Taco object => the method can do whatever with the Taco object
    // returns "redirect:/orders/current" => after the method completes -> the user browser is redirected to the relative path: /orders/current
    // this method now accepts an Order object as a parameter: @ModelAttribute Order order - to indicate that its value
    //                                              should come from the model and that Sprig MVC shouldn't attempt to bind request parameters to it
    // after checking for validation errors, processDesign() uses the injected Taco-Repository to save the taco
    // then adds the Taco object to the Order that's kept in the session
    // the Order object remains in the session and isn’t saved to the database until the user completes and submits the order form
}

// ■■■ Annotations:

// @Slf4j = is a Lombok-provided annotation that at runtime will automatically generate an SLF4J Logger in the class
//  - has the same effect as writing:
//  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DesignTacoController.class);

// @Controller = controller class and makes it a candidate for component scanning => Spring will discover it and automatically create an instance of DesignTacoController as a bean in the Spring application context

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


// if we try to run the application now - when we access localhost:8080/design - we will get: Error resolving template [design], template might not exist or might not be accessible by any of the configured Template Resolvers
// because we don't have a view yet

// ■ Performing validations at form binding:
// @Valid = tells Spring MCV to perform validation on the submitted Taco object after it's bound to the submitted form data  & before the processDesign() method is called

// Chapter 3:
// @SessionAttributes("order") class level annotation = specifies any model objects like the order attribute should be kept in session and available across multiple requests
// @ModelAttribute annotation from method order() (or taco()) = ensures that an Order object will be created in the model
// Unlike the Taco object in the session -> you need the order to be present across multiple requests so that you can create multiple tacos and add them to the order
