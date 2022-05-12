package com.github.xfier.healthtracker.model;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Model goal for user to perform some activity for a set duration.
 *
 * @author Adam Fish
 */
public class DurationGoal extends Goal
{
    /**
     * Duration user intends to be able to reach by set date.
     */
    private final Duration targetDuration;

    public DurationGoal(LocalDate targetDate, String description, Duration targetDuration)
    {
        super(GoalType.DURATION, targetDate, description);
        this.targetDuration = targetDuration;
    }

    public Duration getTargetDuration() { return targetDuration; }
}
