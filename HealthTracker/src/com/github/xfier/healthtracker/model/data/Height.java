package com.github.xfier.healthtracker.model.data;

/**
 * Model person height with validation & utility methods.
 * <p>
 * All values are rounded to the nearest whole number.
 *
 * @author Adam Fish
 */
public class Height
{
    /**
     * Internal height representation (in 10ths of mm - avoid precision loss).
     */
    private final int height;

    /**
     * Create height value from cm.
     *
     * @param cm height in centimetres
     * @throws IllegalArgumentException if the height is out of bounds
     */
    public Height(int cm)
    {
        if (cm < 0 || cm > 300)
            throw new IllegalArgumentException("Height out of bounds: " + cm + "cm");
        height = 100 * cm;
    }

    /**
     * Create height value from feet & inches.
     *
     * @param ft feet
     * @param in inches
     * @throws IllegalArgumentException if the height is out of bounds
     */
    public Height(int ft, int in)
    {
        if (ft < 0 || ft > 9)
            throw new IllegalArgumentException("Invalid height val: " + ft + "ft");
        if (in < 0 || in > 11)
            throw new IllegalArgumentException("Invalid height val: " + in + "in");
        height = (3048 * ft) + (254 * in);
    }

    /**
     * @return height in cm
     */
    public int getCm() { return height/100 + (height % 100)/50; }

    /**
     * @return height in inches
     */
    public int getIn() { return height/254 + (height % 254)/127; }

    @Override
    public String toString() { return getCm() + "cm"; }

    /**
     * Get this height as an imperial string.
     *
     * @return height in ft & in
     */
    public String imperial() { return getIn()/12 + "ft " + getIn()%12 + "in"; }

    /**
     * Test harness.
     *
     * @param args unused
     */
    public static void main(String[] args)
    {
        // test metric -> imperial
        Height h1 = new Height(179);
        Height h2 = new Height(180);
        assert h1.imperial().equals("5ft 10in");
        assert h2.getIn() == (5*12) + 11;

        // test imperial -> metric
        Height ha = new Height(6, 0);
        Height hb = new Height(6, 1);
        assert ha.toString().equals("183cm");
        assert hb.getCm() == 185;
    }
}
