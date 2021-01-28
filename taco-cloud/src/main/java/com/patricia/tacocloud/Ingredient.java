package com.patricia.tacocloud;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
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
}
