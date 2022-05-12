package com.github.xfier.healthtracker.database;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.model.DistanceExercise;
import com.github.xfier.healthtracker.model.DistanceGoal;
import com.github.xfier.healthtracker.model.DurationGoal;
import com.github.xfier.healthtracker.model.Exercise;
import com.github.xfier.healthtracker.model.FoodDiary;
import com.github.xfier.healthtracker.model.Goal;
import com.github.xfier.healthtracker.model.User;
import com.github.xfier.healthtracker.model.WeightExercise;
import com.github.xfier.healthtracker.model.WeightGoal;
import com.github.xfier.healthtracker.model.data.Distance;
import com.github.xfier.healthtracker.model.data.Email;
import com.github.xfier.healthtracker.model.data.Food;
import com.github.xfier.healthtracker.model.data.Height;
import com.github.xfier.healthtracker.model.data.Weight;
import com.github.xfier.healthtracker.model.group.Group;
import com.sun.source.tree.TryTree;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
    Credits:
    https://www.vogella.com/tutorials/MySQLJava/article.html
    https://mariadb.com/kb/en/about-mariadb-connector-j/



 */

public class Database
{
    private Connection connection;

    private String username = "";
    private FoodDiary.Meal meal = FoodDiary.Meal.BREAKFAST; //default

    private int targetGroupId = 1;

    public Database()
    {
        try
        {
            this.connection = DriverManager.getConnection("jdbc:mariadb://localhost/health_tracker?user=root&password=root");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Method to register a user in the database.
     * It is assumed that everything is already validated to this point.
     *
     * @param username The username of the registered user.
     * @param email The email address of the registered user.
     * @param password The password of the registered user.
     * @return {@code true} If the registration was successful. {@code false} Otherwise.
     */
    public boolean registerUser(String username, String email, String password) throws IllegalArgumentException
    {
        try
        {
            PreparedStatement validation = this.connection.prepareStatement("SELECT username, email FROM users WHERE BINARY username = ? OR email = ?;");
            validation.setString(1, username);
            validation.setString(2, email);

            ResultSet resultSet = validation.executeQuery();

            if (resultSet.next())
            {
                throw new IllegalArgumentException("That username and/or email have been taken already!");
            }


            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO health_tracker.users VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, "Your forename");
            statement.setString(5, "Your surname");
            statement.setInt(6, 2000);
            statement.setInt(7, 12);
            statement.setInt(8, 1);
            statement.setInt(9, 100);
            statement.setInt(10, 60);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int getTargetGroupId()
    {
        return targetGroupId;
    }

    public void setTargetGroupId(int targetGroupId)
    {
        this.targetGroupId = targetGroupId;
    }

    public Group getGroup(int id) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT * from groups where id = ?;");

        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next())
        {
            User leader = getUser(resultSet.getString("leader"));
            Group group = new Group(resultSet.getString("name"), leader);
            group.setId(resultSet.getInt("id"));
            group.setMembers(getGroupMembers(id));

            return group;
        }

        throw new IllegalArgumentException("The group with id: " + id + " does not exist!");
    }

    public List<Group> getAllGroups() throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT * from groups;");
        ResultSet resultSet = statement.executeQuery();

        List<Group> groups = new ArrayList<>();

        while (resultSet.next())
        {
            User leader = getUser(resultSet.getString("leader"));
            Group group = new Group(resultSet.getString("name"), leader);
            group.setId(resultSet.getInt("id"));
            group.setMembers(getGroupMembers(resultSet.getInt("id")));

            groups.add(group);
        }

        return groups;
    }

    public FoodDiary getFoodDiary(String username) throws SQLException
    {
        FoodDiary diary = new FoodDiary();
        for (FoodDiary.Meal meal : FoodDiary.Meal.values())
        {
            for (String food : getMealFoods(meal, Main.getDatabase().getUsername()))
            {
                diary.addToMeal(meal, food);
            }
        }

        return diary;
    }

    public Map<String, Food> getSavedFoods(String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT name, calories from saved_foods where username = ?;");
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        Map<String, Food> savedFoods = new HashMap<>();

        while (resultSet.next())
        {
            Food food = new Food(resultSet.getString("name"), resultSet.getInt("calories"));

            savedFoods.put(food.getName(), food);
        }

        return savedFoods;
    }

    public boolean removeSavedFood(String foodName, String username)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM saved_foods where username = ? AND name = ?;");
            statement.setString(1, username);
            statement.setString(2, foodName);
            statement.executeUpdate();
            PreparedStatement mealStatement = this.connection.prepareStatement("DELETE FROM foods where username = ? AND saved_food_name = ?");
            mealStatement.setString(1, username);
            mealStatement.setString(2, foodName);
            mealStatement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean addFoodToMeal(Food food, FoodDiary.Meal meal, String username)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO foods (username, meal, saved_food_name) VALUES(?, ?, ?);");
            statement.setString(1, username);
            statement.setString(2, meal.toString());
            statement.setString(3, food.getName());

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean createSavedFood(Food food, String username)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO saved_foods (username, name, calories) VALUES (?, ?, ?);");
            statement.setString(1, username);
            statement.setString(2, food.getName());
            statement.setInt(3, food.getCalories());

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public List<String> getMealFoods(FoodDiary.Meal meal, String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT saved_food_name from foods where username = ? AND meal = ?;");
        statement.setString(1, username);
        statement.setString(2, meal.toString());

        ResultSet resultSet = statement.executeQuery();

        List<String> foods = new ArrayList<>();

        while (resultSet.next())
        {
            foods.add(resultSet.getString("saved_food_name"));
        }

        return foods;
    }

    public boolean clearMealFoods(FoodDiary.Meal meal, String username)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM foods where username = ? AND meal = ?;");
            statement.setString(1, username);
            statement.setString(2, meal.toString());

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean removeOneMealFood(FoodDiary.Meal meal, String foodName, String username)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM foods WHERE username = ? AND saved_food_name = ? AND meal = ? LIMIT 1;");
            statement.setString(1, username);
            statement.setString(2, foodName);
            statement.setString(3, meal.toString());

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public List<Group> getGroups(String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT group_id from group_members where username = ?;");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        List<Group> groups = new ArrayList<>();
        while (resultSet.next())
        {
            groups.add(getGroup(resultSet.getInt("group_id")));
        }

        return groups;
    }

    public List<User> getGroupMembers(int groupId) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT username from group_members where group_id = ?;");

        statement.setInt(1, groupId);
        ResultSet resultSet = statement.executeQuery();

        List<User> members = new ArrayList<>();
        while (resultSet.next())
        {
            members.add(getUser(resultSet.getString("username")));
        }

        return members;
    }

    //removing member from group when button is selected
    public boolean removeMemberFromGroup(int groupId, String username){
        try{
            System.out.println("GroupID: " + groupId + " Username: " + username);

            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM group_members WHERE username = ? AND group_id = ?;");
            statement.setString(1, username);
            statement.setInt(2, groupId);

            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }

    //adding member to group when button is selected
    public boolean addMemberToGroup(int groupId, String username) throws IllegalArgumentException
    {
        try{

            for (User user : getGroup(groupId).getMembers())
            {
                if (user.getUsername().equals(username))
                {
                    throw new IllegalArgumentException("You are already in that group!");
                }
            }

            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO group_members (group_id, username) VALUES (?, ?);");
            statement.setInt(1, groupId);
            statement.setString(2, username);

            statement.executeUpdate();
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }


    public boolean loginUser(String username, String password)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("SELECT username from users where BINARY username = ? AND BINARY password = ?;");

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                this.username = username;

                return true;
            }
            else
            {
                return false;
            }
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public User getUser(String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT * from users where username = ?;");

        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next())
        {
            PreparedStatement goalStatement = this.connection.prepareStatement("SELECT * from goals where username = ?;");

            goalStatement.setString(1, username);

            ResultSet goalResultSet = goalStatement.executeQuery();

            User user =  new User.Builder(username)
                    .withForename(resultSet.getString("forename"))
                    .withSurname(resultSet.getString("surname"))
                    .withDateOfBirth(LocalDate.of(resultSet.getInt("dob_year"),
                            resultSet.getInt("dob_month"),
                            resultSet.getInt("dob_day")))
                    .withWeight(new Weight(resultSet.getInt("weight")))
                    .withHeight(new Height(resultSet.getInt("height")))
                    .withEmail(new Email(resultSet.getString("email"))).build();

            while (goalResultSet.next())
            {
                try
                {
                    switch (Goal.GoalType.valueOf(goalResultSet.getString("goal_type")))
                    {
                        case WEIGHT -> user.addGoal(new WeightGoal(LocalDate.of(goalResultSet.getInt("year"),
                                goalResultSet.getInt("month"),
                                goalResultSet.getInt("day")),
                                goalResultSet.getString("description"), new Weight(goalResultSet.getInt("weight"))));
                        case DISTANCE -> user.addGoal(new DistanceGoal(LocalDate.of(goalResultSet.getInt("year"),
                                goalResultSet.getInt("month"),
                                goalResultSet.getInt("day")),
                                goalResultSet.getString("description"), new Distance(goalResultSet.getInt("distance"))));
                        case DURATION -> user.addGoal(new DurationGoal(LocalDate.of(goalResultSet.getInt("year"),
                                goalResultSet.getInt("month"),
                                goalResultSet.getInt("day")),
                                goalResultSet.getString("description"), Duration.ofMillis(goalResultSet.getLong("duration"))));
                    }
                }
                catch (IllegalArgumentException e)
                {
                    removeGoal(goalResultSet.getInt("id"));
                    continue;
                }
            }

            return user;
        }

        throw new IllegalArgumentException("The user with username: " + username + " does not exist!");
    }

    public List<Exercise> getExercises(String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT * from exercises where username = ?;");

        statement.setString(1, username);

        List<Exercise> exercises = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next())
        {
            try
            {
                switch (Exercise.ExerciseType.valueOf(resultSet.getString("exercise_type")))
                {
                    case WEIGHT -> {
                        WeightExercise exercise = new WeightExercise(Duration.ofMillis(resultSet.getLong("duration")),
                                new Weight(resultSet.getInt("extra_data")),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                LocalDate.of(resultSet.getInt("year"),
                                        resultSet.getInt("month"),
                                        resultSet.getInt("day")));

                        exercise.setId(resultSet.getInt("id"));
                        exercises.add(exercise);
                    }
                    case RUNNING, WALKING, RIDING, SWIMMING, CROSSFIT -> {
                        DistanceExercise exercise = new DistanceExercise(Exercise.ExerciseType.valueOf(resultSet.getString("exercise_type")),
                                Duration.ofMillis(resultSet.getLong("duration")),
                                new Distance(resultSet.getInt("extra_data")),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                LocalDate.of(resultSet.getInt("year"),
                                        resultSet.getInt("month"),
                                        resultSet.getInt("day")));

                        exercise.setId(resultSet.getInt("id"));
                        exercises.add(exercise);
                    }
                }
            }
            catch (IllegalArgumentException e)
            {
                //TODO Delete the exercise
                continue;
            }
        }

        return exercises;
    }

    public List<Goal> getGoals(String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("SELECT * from goals where username = ?;");
        statement.setString(1, username);

        List<Goal> goals = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next())
        {
            try
            {
                switch (Goal.GoalType.valueOf(resultSet.getString("goal_type")))
                {
                    case WEIGHT -> {
                        WeightGoal goal = new WeightGoal(LocalDate.of(resultSet.getInt("year"),
                                resultSet.getInt("month"),
                                resultSet.getInt("day")),
                                resultSet.getString("description"), new Weight(resultSet.getInt("weight")));

                        goal.setId(resultSet.getInt("id"));

                        goals.add(goal);
                    }
                    case DISTANCE -> {
                        DistanceGoal goal = new DistanceGoal(LocalDate.of(resultSet.getInt("year"),
                                resultSet.getInt("month"),
                                resultSet.getInt("day")),
                                resultSet.getString("description"), new Distance(resultSet.getInt("distance")));

                        goal.setId(resultSet.getInt("id"));

                        goals.add(goal);
                    }
                    case DURATION -> {
                        DurationGoal goal = new DurationGoal(LocalDate.of(resultSet.getInt("year"),
                                resultSet.getInt("month"),
                                resultSet.getInt("day")),
                                resultSet.getString("description"), Duration.ofMillis(resultSet.getLong("duration")));

                        goal.setId(resultSet.getInt("id"));

                        goals.add(goal);
                    }
                }
            }
            catch (IllegalArgumentException e)
            {
                removeGoal(resultSet.getInt("id"));
                continue;
            }
        }

        return goals;
    }

    public boolean removeGoal(int id)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM goals WHERE id = ?;");
            statement.setInt(1, id);

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean addGroup(Group group)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO groups (name, leader) VALUES (?, ?);");
            statement.setString(1, group.getName());
            statement.setString(2, group.getLeader().getUsername());

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean addExercise(Exercise exercise, String username)
    {
        try
        {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO exercises (username, exercise_type, duration, extra_data, name, description, year, month, day) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);");

            statement.setString(1, username);
            statement.setString(2, exercise.getType().toString());
            statement.setLong(3, exercise.getDurationLength().toMillis());
            switch (exercise.getType())
            {
                case WEIGHT -> statement.setInt(4, ((WeightExercise) exercise).getWeight().getKg());
                default -> statement.setInt(4, ((DistanceExercise) exercise).getDistance().getM());
            }
            statement.setString(5, exercise.getName());
            statement.setString(6, exercise.getDescription());
            statement.setInt(7, exercise.getDate().getYear());
            statement.setInt(8, exercise.getDate().getMonthValue());
            statement.setInt(9, exercise.getDate().getDayOfMonth());

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean addGoal(Goal goal, String username)
    {
        try
        {
            PreparedStatement statement = switch (goal.getGoalType())
                    {
                        case WEIGHT -> this.connection.prepareStatement("INSERT INTO goals (username, goal_type, year, month, day, description, weight) VALUES (?, ?, ?, ?, ?, ?, ?);");
                        case DISTANCE -> this.connection.prepareStatement("INSERT INTO goals (username, goal_type, year, month, day, description, distance) VALUES (?, ?, ?, ?, ?, ?, ?);");
                        case DURATION -> this.connection.prepareStatement("INSERT INTO goals (username, goal_type, year, month, day, description, duration) VALUES (?, ?, ?, ?, ?, ?, ?);");
                        default -> null;
                    };

            if (statement == null)
            {
                return false;
            }

            statement.setString(1, username);
            statement.setString(2, goal.getGoalType().toString());
            statement.setInt(3, goal.getTargetDate().getYear());
            statement.setInt(4, goal.getTargetDate().getMonthValue());
            statement.setInt(5, goal.getTargetDate().getDayOfMonth());
            statement.setString(6, goal.getDescription());
            switch (goal.getGoalType())
            {
                case DURATION -> statement.setLong(7, ((DurationGoal) goal).getTargetDuration().toMillis());
                case DISTANCE -> statement.setInt(7, ((DistanceGoal) goal).getTargetDistance().getM());
                case WEIGHT -> statement.setInt(7, ((WeightGoal) goal).getTargetWeight().getKg());
            }

            statement.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void updateForeAndSurnames(String forename, String surname, String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("UPDATE users SET forename = ?, surname = ? WHERE username = ?;");

        statement.setString(1, forename);
        statement.setString(2, surname);
        statement.setString(3, username);

        statement.executeUpdate();
    }

    public void setDateOfBirth(LocalDate localDate, String username) throws SQLException
    {
        PreparedStatement statement = this.connection.prepareStatement("UPDATE users SET dob_year = ?, dob_month = ?, dob_day = ? WHERE username = ?;");

        statement.setInt(1, localDate.getYear());
        statement.setInt(2, localDate.getMonthValue());
        statement.setInt(3, localDate.getDayOfMonth());
        statement.setString(4, username);

        statement.executeUpdate();
    }

    public void setWeight(int weight, String username) throws SQLException
    {
        PreparedStatement statement =  this.connection.prepareStatement("UPDATE users SET weight = ? WHERE username = ?;");

        statement.setInt(1, weight);
        statement.setString(2, username);

        statement.executeUpdate();
    }

    public void setHeight(int height, String username) throws SQLException
    {
        PreparedStatement statement =  this.connection.prepareStatement("UPDATE users SET height = ? WHERE username = ?;");

        statement.setInt(1, height);
        statement.setString(2, username);

        statement.executeUpdate();
    }

    public String getUsername()
    {
        return username;
    }

    public void setMeal(FoodDiary.Meal meal)
    {
        this.meal = meal;
    }

    public FoodDiary.Meal getMeal()
    {
        return meal;
    }
}
