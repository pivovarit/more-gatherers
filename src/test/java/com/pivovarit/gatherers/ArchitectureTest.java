package com.pivovarit.gatherers;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.JavaModifier.FINAL;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.pivovarit", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule shouldHaveSingleFacade = classes()
      .that().arePublic()
      .should().haveSimpleName("MoreGatherers")
      .andShould().haveOnlyFinalFields()
      .andShould().haveOnlyPrivateConstructors()
      .andShould().haveModifier(FINAL)
      .as("all public factory methods should be accessed through MoreGatherers public API")
      .because("users of MoreGatherers should have a single entry point");

    @ArchTest
    static final ArchRule shouldHaveZeroDependencies = classes()
      .that().resideInAPackage("com.pivovarit.gatherers")
      .should()
      .onlyDependOnClassesThat()
      .resideInAnyPackage("com.pivovarit.gatherers", "org.jspecify.annotations", "java..")
      .as("the library should depend only on core Java classes")
      .because("users appreciate not experiencing a dependency hell");

    @ArchTest
    static final ArchRule shouldHaveSinglePackage = classes()
      .should().resideInAPackage("com.pivovarit.gatherers");
}
