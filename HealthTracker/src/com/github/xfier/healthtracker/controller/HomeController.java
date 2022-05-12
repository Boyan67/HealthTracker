package com.github.xfier.healthtracker.controller;


import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.Exercise;
import com.github.xfier.healthtracker.model.Goal;
import com.github.xfier.healthtracker.model.User;
import com.github.xfier.healthtracker.model.WeightGoal;
import com.github.xfier.healthtracker.model.data.Weight;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController implements Initializable{


    @FXML
    DatePicker StartDate = new DatePicker();

    @FXML
    DatePicker EndDate = new DatePicker();

    @FXML
    Pane barChartPane;

    @FXML
    Label nameLabel;

    @FXML
    Label lblCurrentWeight;

    @FXML
    Label lblGoalWeight;

    @FXML
    ProgressBar progressBar;
    /* All Controllers need the functionality to be able to swap between pages,
       so these 3 button events will be in each of the controllers.
     */

    public void HomeButtonPushed(javafx.event.ActionEvent event) throws Exception
    {
        Parent HomeParent = FXMLLoader.load(getClass().getResource("../view/HomePage.fxml"));
        Scene HomeScene = new Scene(HomeParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(HomeScene);
        window.show();

    }
    public void ProfileButtonPushed(javafx.event.ActionEvent event) throws Exception
    {

        Parent ProfileParent = FXMLLoader.load(getClass().getResource("../view/ProfilePage.fxml"));
        Scene ProfileScene = new Scene(ProfileParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(ProfileScene);
        window.show();
    }
    public void ExerciseButtonPushed(javafx.event.ActionEvent event) throws Exception
    {

        Parent ExerciseParent = FXMLLoader.load(getClass().getResource("../view/ExercisePage.fxml"));
        Scene ExerciseScene = new Scene(ExerciseParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(ExerciseScene);
        window.show();
    }

    public void DietButtonPushed(javafx.event.ActionEvent event) throws Exception
    {
        Parent DietParent = FXMLLoader.load(getClass().getResource("../view/DietPage.fxml"));
        Scene DietScene = new Scene(DietParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(DietScene);
        window.show();
    }

    public void GroupButtonPushed(javafx.event.ActionEvent event) throws Exception
    {
        Parent GroupParent = FXMLLoader.load(getClass().getResource("../view/GroupPage.fxml"));
        Scene GroupScene = new Scene(GroupParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(GroupScene);
        window.show();
    }
    public void GoalsButtonPushed(javafx.event.ActionEvent event) throws Exception
    {

        Parent GoalsParent = FXMLLoader.load(getClass().getResource("../view/GoalsPage.fxml"));
        Scene GoalsScene = new Scene(GoalsParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(GoalsScene);
        window.show();
    }

    public void DatesUpdated(javafx.event.ActionEvent event) throws Exception
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InitializeHomePage();
    }

    // initialises values in the profile page, loading a user and adds the options for the choice box
    public void InitializeHomePage()
    {
        CategoryAxis xAxis = new CategoryAxis();

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Time (minutes)");

        BarChart barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("All time exercise durations");

        XYChart.Series data = new XYChart.Series();
        data.setName("Exercise Time");

        try
        {
            //TODO Currently is for all time, maybe change this to support a weekly/monthly option button.
            Map<Exercise.ExerciseType, Long> exerciseTotals = new HashMap<>();

            for (Exercise exercise : Main.getDatabase().getExercises(Main.getDatabase().getUsername()))
            {
                exerciseTotals.put(exercise.getType(), exerciseTotals.getOrDefault(exercise.getType(), 0L) + exercise.getDurationLength().toMinutes());
            }

            for (Exercise.ExerciseType type : Exercise.ExerciseType.values())
            {
                data.getData().add(new XYChart.Data(type.toString(), exerciseTotals.getOrDefault(type, 0L)));
            }

            barChart.getData().add(data);
        }
        catch (SQLException e)
        {
            AlertBox.display("Error", "Failed to load your exercises, please try to reload the page again later...");
        }

        barChartPane.getChildren().add(barChart);

        // get goal weight - most recent weight goal (if present)
        WeightGoal closestGoal = new WeightGoal(LocalDate.MAX, "Default.", new Weight(60));
        try
        {
            List<Goal> goals = Main.getDatabase().getGoals(Main.getDatabase().getUsername());
            for (Goal g : goals)
            {
                if (g instanceof WeightGoal)
                {
                    WeightGoal wg = (WeightGoal)g;
                    if (wg.getTargetDate().isBefore(closestGoal.getTargetDate()))
                    {
                        closestGoal = wg;
                    }
                }
            }
        }
        catch (SQLException ignored) { }
        Weight goalWeight = closestGoal.getTargetWeight();

        try
        {
            User user = Main.getDatabase().getUser(Main.getDatabase().getUsername());
            nameLabel.setText("Welcome, " + user.getForename() + " " + user.getSurname());
            if(user.getWeight().getKg() > goalWeight.getKg()){
                double weightDiff = user.getWeight().getKg() - goalWeight.getKg();
                progressBar.setProgress(1 - weightDiff/goalWeight.getKg());
            }else{
                progressBar.setProgress(user.getWeight().getKg()/(double)goalWeight.getKg());
            }
            lblCurrentWeight.setText(user.getWeight().toString());
            lblGoalWeight.setText(goalWeight.toString());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
