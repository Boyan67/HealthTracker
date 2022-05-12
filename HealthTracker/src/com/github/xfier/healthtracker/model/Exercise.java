package com.github.xfier.healthtracker.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Model abstract Exercise.
 *
 * Exercise information is immutable, so cannot be changed. The ID value may be updated because it's defined by the database.
 * This ensures sharing and exposing exercises from one user to another cannot result in accidental modification;
 */
public abstract class Exercise
{
    /**
     * ID value of the goal.
     */
    private int id;

    /**
     * Type of the exercise;
     */
    private ExerciseType type;

    /**
     * Duration legnth of the exercise.
     */
    private final Duration durationLength;

    /**
     * When the exercise happened.
     */
    private final LocalDate date;

    /**
     * User defined name of the exercise.
     */
    private final String name;

    /**
     * User defined description of the exercise.
     */
    private final String description;

    /**
     * Creates a new Exercise.
     *
     * @param durationLength Exercise duration
     * @param name Name
     * @param description Description
     */
    public Exercise(ExerciseType type, Duration durationLength, String name, String description, LocalDate date)
    {
        if (name == null || description == null || type == null)
        {
            throw new NullPointerException();
        }

        this.type = type;
        this.durationLength = durationLength;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    @Override
    public String toString()
    {
        return getDescription();
    }

    public LocalDate getDate()
    {
        return date;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Duration getDurationLength()
    {
        return durationLength;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public ExerciseType getType()
    {
        return type;
    }

    public enum ExerciseType
    {
        RUNNING, WALKING, RIDING, SWIMMING, CROSSFIT, WEIGHT;
    }
}
