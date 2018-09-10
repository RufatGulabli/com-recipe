package com.recipe.controllers;

import com.recipe.domain.*;
import com.recipe.repositories.CategoryRepository;
import com.recipe.repositories.RecipeRepository;
import com.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {



    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(RecipeRepository recipeRepository, CategoryRepository categoryRepository,
                           UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("loading recipes ...");
    }

    public List<Recipe> getRecipes(){

        List<Recipe> recipes = new ArrayList<>();

        Optional<UnitOfMeasure> teaSpoon = unitOfMeasureRepository.findByDescription("Teaspoon");
        Optional<UnitOfMeasure> tableSpoon = unitOfMeasureRepository.findByDescription("Tablespoon");
        Optional<UnitOfMeasure> cup = unitOfMeasureRepository.findByDescription("Cup");
        Optional<UnitOfMeasure> pinch = unitOfMeasureRepository.findByDescription("Pinch");
        Optional<UnitOfMeasure> ounce = unitOfMeasureRepository.findByDescription("Ounce");
        Optional<UnitOfMeasure> each = unitOfMeasureRepository.findByDescription("Each");
        Optional<UnitOfMeasure> dash = unitOfMeasureRepository.findByDescription("Dash");
        Optional<UnitOfMeasure> pint = unitOfMeasureRepository.findByDescription("Pint");

        if(!(teaSpoon.isPresent() || tableSpoon.isPresent() || cup.isPresent() || pinch.isPresent() || ounce.isPresent()
            || each.isPresent() || dash.isPresent() || pint.isPresent())){
            throw new RuntimeException("Expected UOM not found !");
        }

        Optional<Category> american = categoryRepository.findByDescription("American");
        Optional<Category> mexican = categoryRepository.findByDescription("Mexican");
        Optional<Category> italian = categoryRepository.findByDescription("Italian");
        Optional<Category> fastFood = categoryRepository.findByDescription("Fast Food");

        if(!(american.isPresent() || mexican.isPresent() || italian.isPresent() || fastFood.isPresent())){
            throw new RuntimeException("Expected category not found");
        }

        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. " +
                "Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. ");

        Notes notes = new Notes();
        notes.setRecipeNotes("For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.");
        guacRecipe.setNotes(notes);
        guacRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), each.get()));
        guacRecipe.addIngredient(new Ingredient("Kosher salt", new BigDecimal(5), teaSpoon.get()));
        guacRecipe.addIngredient(new Ingredient("fresh lime juice or limon juice", new BigDecimal(2), tableSpoon.get()));

        guacRecipe.getCategories().add(american.get());
        guacRecipe.getCategories().add(mexican.get());

        recipes.add(guacRecipe);

        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription("Spicy Grilled Chicken Taco");
        tacosRecipe.setPrepTime(5);
        tacosRecipe.setCookTime(2);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);

        Notes notes1 = new Notes();
        notes1.setRecipeNotes("For a very quick guacamole mgjkgjm bjmgbjk gjkgghk" +
                " just take a 1/4 cup of salsa and mix it in with your mashed avocados.");
        tacosRecipe.setNotes(notes1);
        tacosRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), each.get()));
        tacosRecipe.addIngredient(new Ingredient("Kosher salt", new BigDecimal(5), teaSpoon.get()));
        tacosRecipe.addIngredient(new Ingredient("fresh lime juice or limon juice", new BigDecimal(2), tableSpoon.get()));

        tacosRecipe.getCategories().add(fastFood.get());
        tacosRecipe.getCategories().add(italian.get());

        recipes.add(tacosRecipe);


        return recipes;
    }
}
