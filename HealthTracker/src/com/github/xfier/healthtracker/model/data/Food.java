package com.github.xfier.healthtracker.model.data;

import java.util.Objects;

/**
 * Model single food/drink item in food diary.
 * @author Adam Fish
 */
public class Food
{
    /**
     * Name of this food.
     */
    private final String name;

    /**
     * Number of calories in 1 portion.
     */
    private final int calories;

    /**
     * Create new food/drink item.
     * @param name food name
     * @param calories calories
     */
    public Food(String name, int calories)
    {
        if (name == null)
            throw new NullPointerException();
        if (calories < 0 || calories > 10000)
            throw new IllegalArgumentException("Calorie count out of bounds: " + calories);

        this.name = name;
        this.calories = calories;
    }

    /**
     * @return a list of default foods.
     */
    public static Food[] getDefaultFoods()
    {
        // sample foods from https://www.uncledavesenterprise.com/file/health/Food%20Calories%20List.pdf
        return new Food[] {
                new Food("Apple", 44),
                new Food("Banana", 107),
                new Food("Lettuce", 4),
                new Food("Orange", 40),
                new Food("Pear", 45),

                new Food("Bacon", 250),
                new Food("Chicken", 220),
                new Food("Ham", 6),
                new Food("Roast Beef", 300),
                new Food("Sausage Roll", 290),

                new Food("Bagel", 140),
                new Food("Brown Rice", 405),
                new Food("White Bread", 96),
                new Food("White Rice", 420),
                new Food("Wholemeal Bread", 88),

                new Food("Chocolate", 200),
                new Food("Egg", 90),
                new Food("Glass of Milk (full-fat)", 175),
                new Food("Glass of Milk (semi-skimmed)", 125)
        };
    }

    public String getName()  { return name;     }
    public int getCalories() { return calories; }

    @Override
    public String toString() { return getName() + " [" + getCalories() + " kcal]"; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return calories == food.calories && name.equals(food.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name, calories); }

    /**
     * Test harness.
     * @param args unused
     */
    public static void main(String[] args)
    {
        Food f1 = new Food("Banana", 89);
        System.out.println(f1);
    }
}
