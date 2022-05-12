package com.github.xfier.healthtracker.model.data;

/**
 * Model person weight with validation & utility methods.
 * <p>
 * All values are rounded to the nearest whole number.
 * @author Adam Fish
 */
public class Weight
{
    /**
     * Internal weight representation (in 10ths of g - avoid precision loss).
     */
    private final int weight;

    /**
     * Create weight value from kg.
     * @param kg weight in kilograms
     * @throws IllegalArgumentException if the weight is out of bounds
     */
    public Weight(int kg)
    {
        if (kg < 0 || kg > 500)
            throw new IllegalArgumentException("Weight out of bounds: " + kg + "kg");
        weight = 10000 * kg;
    }

    /**
     * Create weight value from st & lb.
     * @param st stone
     * @param lb pounds
     * @throws IllegalArgumentException if the weight is out of bounds
     */
    public Weight(int st, int lb)
    {
        if (st < 0 || st > 80)
            throw new IllegalArgumentException("Weight out of bounds: " + st + "st");
        if (lb < 0 || lb > 13)
            throw new IllegalArgumentException("Weight out of bounds (for fraction of 1st): " + lb + "lb");
        weight = (63503 * st) + (4536 * lb);
    }

    /**
     * @return weight in kg
     */
    public int getKg() { return weight/10000 + (weight % 10000)/5000; }

    /**
     * @return weight in lb
     */
    public int getLb() { return weight/4536 + (weight % 4536)/2268; }

    @Override
    public String toString() { return getKg() + "kg"; }

    /**
     * Get this weight as an imperial string.
     * @return weight in st & lb
     */
    public String imperial() { return getLb()/14 + "st " + getLb()%14 + "lb"; }

    /**
     * Test harness.
     * @param args unused
     */
    public static void main(String[] args)
    {
        // test metric -> imperial
        Weight w1 = new Weight(65);
        Weight w2 = new Weight(66);
        assert w1.imperial().equals("10st 3lb");
        assert w2.getLb() == (10*14) + 6;

        // test imperial -> metric
        Weight wa = new Weight(9, 7);
        Weight wb = new Weight(9, 8);
        assert wa.toString().equals("60kg");
        assert wb.getKg() == 61;
    }
}
