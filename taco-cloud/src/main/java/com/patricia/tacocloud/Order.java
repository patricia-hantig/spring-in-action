package com.patricia.tacocloud;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Taco_Order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            // field that uniquely identifies the object

    private Date placedAt;     // filed when an Order is placed

    @NotBlank(message = "Delivery Name is required")
    private String deliveryName;

    @NotBlank(message = "Delivery Street is required")
    private String deliveryStreet;

    @NotBlank(message = "Delivery City is required")
    private String deliveryCity;

    @NotBlank(message = "Delivery State is required")
    private String deliveryState;

    @NotBlank(message = "Delivery Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;
    // ex of a valid card number: 4111111111111111

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message="Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    @ManyToMany(targetEntity = Taco.class)
    private List<Taco> tacos = new ArrayList<>();

    public void addDesign(Taco design) {
        this.tacos.add(design);
    }

    @PrePersist
    void placedAt() {
        this.placedAt = new Date();
    }
}

// ■ Validating form input:
// @NotNull - property isn't empty or null
// @CreditCardNumber - property value must be a valid credit card number that passes Luhn algorithm check
//                  - located in org.hibernate.validator package
// @Pattern(regex="...') - is used to create pattern "MM/YY" for expiration date of the credit card
// @Digits(integer=3, fraction=0, message="...") - property's value is exact 3 digits otherwise message is displayed
// - except @CreditCardNumber all the other annotations are located within javax.validation.constants package

// ■■ Annotations chapter3_part_2
// @Entity  = marks the class as a JPA entity
//          - an entity represents a table in a relational database
//          - by have the class as Entity - we don't need to manually create the table using schema.sql - it creates the table automatically & also the needed tables for solving a manyToMany relationship
// @Table   = specifies that Order entities should be persisted to Taco_Order table in database
//          - it's necessary with Order because without it, JPA would default to persisting the entities to a table named Order
//          (order is a reserved word in SQL)
// @Id = the property will uniquely identify the entity in the database
// @GeneratedValue(strategy = GenerationType.AUTO) = because we use a database which automatically generates the id value
// @ManyToMany(targetEntity = Ingredient.class) - declare the relationship between an Order & its associated Taco list
//                                              = An Order can have many Taco objects & an Taco can be part of many Orders
// @PrePersist - we use it to set the property placedAt to the current date and time before Order is persisted
