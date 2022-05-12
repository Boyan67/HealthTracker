package com.github.xfier.healthtracker.model;

import com.github.xfier.healthtracker.model.data.Distance;

import java.time.LocalDate;

/**
 * Model goal for user to run set distance by a certain date.
 *
 * @author Adam Fish
 */
public class DistanceGoal extends Goal
{
    /**
     * Distance user intends to run by set date.
     */
    private final Distance targetDistance;

    public DistanceGoal(LocalDate targetDate, String description, Distance targetDistance)
    {
        super(GoalType.DISTANCE, targetDate, description);
        this.targetDistance = targetDistance;
    }

    public Distance getTargetDistance() { return targetDistance; }
}
