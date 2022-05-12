package com.github.xfier.healthtracker.model;

import com.github.xfier.healthtracker.model.data.Distance;

import java.time.Duration;
import java.time.LocalDate;

public class DistanceExercise extends Exercise
{
    private Distance distance;

    public DistanceExercise(ExerciseType type, Duration durationLength, Distance distance, String name, String description, LocalDate date)
    {
        super(type, durationLength, name, description, date);
        if (distance == null || date == null)
        {
            throw new NullPointerException();
        }

        this.distance = distance;
    }

    public Distance getDistance()
    {
        return distance;
    }
}
