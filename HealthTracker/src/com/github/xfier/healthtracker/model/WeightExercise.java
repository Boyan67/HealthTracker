package com.github.xfier.healthtracker.model;

import com.github.xfier.healthtracker.model.data.Weight;

import java.time.Duration;
import java.time.LocalDate;

public class WeightExercise extends Exercise
{
    private Weight weight;

    public WeightExercise(Duration durationLength, Weight weight, String name, String description, LocalDate date)
    {
        super(ExerciseType.WEIGHT, durationLength, name, description, date);
        if (weight == null || date == null)
        {
            throw new NullPointerException();
        }

        this.weight = weight;
    }

    public Weight getWeight()
    {
        return weight;
    }
}
