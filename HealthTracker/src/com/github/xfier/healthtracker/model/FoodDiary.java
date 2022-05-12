package com.github.xfier.healthtracker.model;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.model.data.Food;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Model user food diary, with a mutable collection of foods for each meal.
 * @author Adam Fish
 */
public class FoodDiary
{
    /**
     * All meal types.
     */
    public enum Meal { BREAKFAST, LUNCH, DINNER }

    /**
     * Map of currently saved food items.
     */
    private final Map<String, Food> foods;

    /**
     * Foods eaten at each meal.
     */
    private final List<Food> breakfast;
    private final List<Food> lunch;
    private final List<Food> dinner;

    /**
     * Create new food diary loaded with a default list of foods.
     */
    public FoodDiary()
    {
        foods = Arrays.stream(Food.getDefaultFoods()).collect(Collectors.toMap(Food::getName, Function.identity()));
        try
        {
            foods.putAll(Main.getDatabase().getSavedFoods(Main.getDatabase().getUsername())); //Overrides the defaults with user-defined ones
        }
        catch (SQLException ignored) {}

        breakfast = new ArrayList<>();
        lunch = new ArrayList<>();
        dinner = new ArrayList<>();
    }

    /**
     * @return the food with the given name.
     */
    public Food getFood(String name)
    {
        return foods.get(name);
    }

    /**
     * @return the list of all food names.
     */
    public Set<String> getFoods()
    {
        return foods.keySet();
    }

    public Collection<Food> getFoodsList()
    {
        return foods.values();
    }

    /**
     * Add food item to the list of all foods.
     */
    public void addFood(Food f) { foods.put(f.getName(), f); }

    /**
     * Remove food item from the list of all foods.
     */
    public boolean removeFood(Food f) { return foods.remove(f.getName()) != null; }

    /**
     * Get list corresponding to specified meal.
     */
    private List<Food> getMealList(Meal m)
    {
        return switch (m)
        {
            case BREAKFAST -> breakfast;
            case LUNCH     -> lunch;
            case DINNER    -> dinner;
        };
    }

    /**
     * Add food to meal.
     */
    public boolean addToMeal(Meal m, String food)
    {
        Food f = foods.get(food);
        if (f == null)
            throw new IllegalArgumentException(food + " not in list of saved foods.");
        return getMealList(m).add(f);
    }

    /**
     * Remove food from meal.
     */
    public boolean removeFromMeal(Meal m, String food)
    {
        Food f = foods.get(food);
        if (f == null)
            throw new IllegalArgumentException(food + " not in list of saved foods.");
        return getMealList(m).remove(f);
    }

    /**
     * Get immutable list view of meal.
     */
    public List<Food> getMeal(Meal m) { return Collections.unmodifiableList(getMealList(m)); }

    @Override
    public String toString()
    {
        return "ALL       : " + getFoods() +
               "\nBREAKFAST : " + getMeal(Meal.BREAKFAST) +
               "\nLUNCH     : " + getMeal(Meal.LUNCH) +
               "\nDINNER    : " + getMeal(Meal.DINNER);
    }

    /**
     * Test harness.
     * @param args unused
     */
    public static void main(String[] args)
    {
        FoodDiary f = new FoodDiary();
        System.out.println(f);

        f.addToMeal(Meal.LUNCH, "Apple");
        System.out.println(f);

        f.removeFromMeal(Meal.LUNCH, "Apple");
    }
}
