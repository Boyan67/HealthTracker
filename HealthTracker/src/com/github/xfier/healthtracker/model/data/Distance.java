package com.github.xfier.healthtracker.model.data;

/**
 * Model distance with validation.
 *
 * @author Adam Fish
 */
public class Distance
{
    /**
     * Internal distance representation (in m).
     */
    private final int distance;

    /**
     * Create distance value from metres.
     *
     * @param m metres
     * @throws IllegalArgumentException if the distance is out of bounds
     */
    public Distance(int m)
    {
        if (m < 0 || m > 16100000)
            throw new IllegalArgumentException("Height out of bounds: " + m + "m");
        this.distance = m;
    }

    /**
     * Create distance value from km.
     *
     * @param km kilometres
     * @return new Distance
     * @throws IllegalArgumentException if the distance is out of bounds
     */
    public static Distance fromKm(int km) { return new Distance(1000 * km); }

    /**
     * Create distance value from miles.
     *
     * @param miles miles
     * @return new Distance
     * @throws IllegalArgumentException if the distance is out of bounds
     */
    public static Distance fromMiles(int miles) { return new Distance(1610 * miles); }

    /**
     * @return distance in metres
     */
    public int getM() { return distance; }

    /**
     * @return distance in kilometres
     */
    public int getKm() { return distance/1000 + (distance % 1000)/500; }

    /**
     * @return distance in miles
     */
    public int getMiles() { return distance/1610 + (distance % 1610)/805; }

    @Override
    public String toString() { return getM() + "m"; }

    /**
     * Test harness.
     * @param args unused
     */
    public static void main(String[] args)
    {
        Distance d1 = new Distance(100);
        System.out.println(d1 + " - " + d1.getKm() + " - " + d1.getMiles());
        Distance d2 = Distance.fromKm(5);
        System.out.println(d2 + " - " + d2.getKm() + " - " + d2.getMiles());
        Distance d3 = Distance.fromMiles(26);
        System.out.println(d3 + " - " + d3.getKm() + " - " + d3.getMiles());
    }
}
