package com.github.xfier.healthtracker.model;

import com.github.xfier.healthtracker.model.data.Weight;

import java.time.LocalDate;

/**
 * Model goal for user to reach target weight by a certain date.
 *
 * @author Adam Fish
 */
public class WeightGoal extends Goal
{
    /**
     * Weight a user intends to reach by the goal date.
     */
    private final Weight targetWeight;

    public WeightGoal(LocalDate targetDate, String description, Weight targetWeight)
    {
        super(GoalType.WEIGHT, targetDate, description);
        this.targetWeight = targetWeight;
    }

    public Weight getTargetWeight() { return targetWeight; }
}
