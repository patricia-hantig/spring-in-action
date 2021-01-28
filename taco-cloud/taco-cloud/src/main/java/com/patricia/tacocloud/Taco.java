package com.patricia.tacocloud;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Taco {

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<String> ingredients;
}

// @Data - automatically generates essential JavaBean methods for you at runtime
// The fields in Taco class corresponds to the checkbox elements defined in design.html view:
// name from Taco.java          ~ <span th:text="${ingredient.name}" ... </span>
// ingredients from Taco.java   ~ <input name = "ingredients" type="checkbox" ... />

// ■ Validating form input:
// @NotNull - property isn't empty or null
// @Size(min=5, message="...") = property's value should have at least 5 characters otherwise message is displayed
// - both annotations are in package javax.validation.constants
