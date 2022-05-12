package com.github.xfier.healthtracker.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Model abstract User goal.
 * <p>
 * Goal information is immutable, so cannot be changed once created, however the ID value can be changed,
 * such as when the ID is provided by the database.
 * This ensures sharing & exposing goals from one user cannot result in accidental goal
 * modification.
 *
 * @author Adam Fish, Brandon Gous
 */
public abstract class Goal implements Comparable<Goal>
{
    /**
     * Numerical representation of the {@link Goal goal's} ID value.
     */
    private int id;

    /**
     * The type that this goal is.
     */
    private final GoalType goalType;

    /**
     * Date to complete goal by.
     */
    private final LocalDate targetDate;

    /**
     * Short description of goal.
     */
    private final String description;

    /**
     * Create new goal.
     *
     * @param goalType goal type
     * @param targetDate target date
     * @param description description of goal
     * @throws IllegalArgumentException if targetDate is in the past
     */
    public Goal(GoalType goalType, LocalDate targetDate, String description)
    {
        if (targetDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Goal target date cannot be in the past");
        if (description == null)
            throw new NullPointerException();

        this.goalType = goalType;
        this.targetDate = targetDate;
        this.description = description;
    }

    public GoalType getGoalType()
    {
        return goalType;
    }
    public LocalDate getTargetDate() { return targetDate;  }
    public String getDescription()   { return description; }
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return whether goal date is in the past.
     */
    public boolean isExpired()       { return LocalDate.now().isAfter(targetDate); }

    @Override
    public String toString() { return getDescription(); }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return targetDate.equals(goal.targetDate) && description.equals(goal.description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(targetDate, description);
    }

    @Override
    public int compareTo(Goal o)
    {
        return getId() - o.getId();
    }

    public enum GoalType
    {
        WEIGHT, DURATION, DISTANCE;
    }
}
