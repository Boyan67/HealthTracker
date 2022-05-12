package com.github.xfier.healthtracker.model.group;

import com.github.xfier.healthtracker.model.Goal;
import com.github.xfier.healthtracker.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * STUB
 *
 * @author Brandon Gous
 */
public class Group
{
    private int id;

    private String name;

    private User leader;

    private List<Goal> goals;

    private List<User> members = new ArrayList<>();

    public Group(String name, User leader)
    {
        this.name = name;
        this.leader = leader;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public User getLeader()
    {
        return leader;
    }

    public List<Goal> getGoals()
    {
        return goals;
    }

    public List<User> getMembers()
    {
        return members;
    }

    public void setMembers(List<User> members)
    {
        this.members = members;
    }
}
