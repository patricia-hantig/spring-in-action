package tacos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
public class Ingredient {

    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}

// there are NO getter and setter methods, equals(), hashCode(), toString() + others
// we don't need to write them because by using the library Lombok => it will automatically generate those methods at runtime
// @Data = is provided by Lombok and tells Lombok to generate all of those missing methods + a constructor that accepts all final properties as arguments
// to use Lombok - add the dependency into the pom.xml file

// You will also need to add Lombok as an extension in your IDE - otherwise the IDE will complain with errors about missing methods and final properties that aren't being set
// ■■ Add Lombok as an extension to IntelliJ IDEA:
//  - step 1: enable Annotation Processing
//              Lombok uses annotation processing through APT, so, when the compiler calls it, the library generates new source files based on annotations in the originals
//              Annotation processing isn't enabled by default
//      File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors and make sure of the following:
//          - Enable annotation processing box is checked
//          - Obtain processors from project classpath option is selected
//  - step 2: installing the IDE Plugin
//              There is a dedicated plugin which makes IntelliJ aware of the source code to be generated. After installing it, the errors go away and regular features like Find Usages, Navigate To start working
//      File | Settings | Plugins, open the Marketplace tab, type lombok and choose Lombok Plugin by Michail Plushnikov
//      Next, click the Install button on the plugin page
//      click the Restart IDE button

// ■■ Annotations chapter3_part_2
// @Entity  = marks the class as a JPA entity
//          - an entity represents a table in a relational database
//          - by have the class as Entity - we don't need to manually create the table using schema.sql - it creates the table automatically & also the needed tables for solving a manyToMany relationship
// @Id = the property will uniquely identify the entity in the database
// @NoArgsConstructor   = annotation at the class level, Lombok annotation
//                      - JPA requires that entities have a no-argument constructor
// @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)   -> you won't be able to use the constructor that's why the access is set to private
//                                                                  -> because there are final properties that must be set => you set the force attribute to true
//                                                                  => results a private Lombok-generated no-argument constructor which sets the final properties to null
// @RequiredArgsConstructor = comes implicit with @Data annotation, but when a @NoArgsConstructor is used -> that constructor gets removed
//                          - needs to be explicit mentioned = ensures that you'll still have a required arguments constructor in addition to the private no-args constructor


