package tacos;

import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@RestResource(rel = "tacos", path = "tacos")
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            // field that uniquely identifies the object

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    private Date createdAt;     // filed when a Taco is created

    @ManyToMany(targetEntity = Ingredient.class)
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients = new ArrayList<>();

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}

// @Data - automatically generates essential JavaBean methods for you at runtime
// The fields in Taco class corresponds to the checkbox elements defined in design.html view:
// name from Taco.java          ~ <span th:text="${ingredient.name}" ... </span>
// ingredients from Taco.java   ~ <input name = "ingredients" type="checkbox" ... />

// ■ Validating form input:
// @NotNull - property isn't empty or null
// @Size(min=5, message="...") = property's value should have at least 5 characters otherwise message is displayed
// - both annotations are in package javax.validation.constants

// ■■ Annotations chapter3_part_2
// @Entity  = marks the class as a JPA entity
//          - an entity represents a table in a relational database
//          - by have the class as Entity - we don't need to manually create the table using schema.sql - it creates the table automatically & also the needed tables for solving a manyToMany relationship
// @Id = the property will uniquely identify the entity in the database
// @GeneratedValue(strategy = GenerationType.AUTO) = because we use a database which automatically generates the id value
// @ManyToMany(targetEntity = Ingredient.class) - declare the relationship between a Taco & its associated Ingredient list
//                                              = A Taco can have many Ingredient objects & an Ingredient can be part of many Tacos
// @PrePersist - we use it to set the property createdAt to the current date and time before Taco is persisted

// @RestResource(rel = "tacos", path = "tacos") = lets you give the entity any relation name and path you want to be in the requests of the API