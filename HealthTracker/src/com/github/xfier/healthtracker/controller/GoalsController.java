package com.github.xfier.healthtracker.controller;
import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Duration;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GoalsController implements Initializable {


    @FXML private ListView GoalList = new ListView();
    @FXML private TableView<Goal> GoalTable = new TableView();

    @FXML private Label TypeLabel = new Label();
    @FXML private Label ValueLabel = new Label();
    @FXML private Label TDateLabel = new Label();
    @FXML private Label DescriptionLabel = new Label();


    private List<Goal> goals = new ArrayList<>();
    private int selectedGoal = -1;

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

    public void AddGoalPushed() throws Exception
    {
        Parent AddGoalParent = FXMLLoader.load(getClass().getResource("../view/AddGoalPage.fxml"));
        Scene AddGoalScene = new Scene(AddGoalParent,700,500);


        Stage window = new Stage();
        window.setScene(AddGoalScene);

        window.setOnCloseRequest(event ->
        {
            InitializeProfilePage();
        });

        window.show();

    }

    public void RemoveGoal() throws Exception
    {
        if (getSelectedGoal() == -1)
        {
            return;
        }

        if (Main.getDatabase().removeGoal(this.goals.get(getSelectedGoal()).getId()))
        {
            this.goals.remove(getSelectedGoal());
            setSelectedGoal(-1);

            Collections.sort(goals);
            GoalList.getItems().clear();

            for (Goal goal : this.goals)
            {
                GoalList.getItems().add(goal.getDescription());
            }
        }
        else
        {
            //TODO Failed
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitializeProfilePage();
    }

    public ObservableList<Goal> LoadGoal()
    {
        ObservableList<Goal> Goals = FXCollections.observableArrayList();
        Goals.add(new DurationGoal(LocalDate.now(),"Description", Duration.ofDays(23)));
        return Goals;
    }


    // initialises values in the profile page, loading a user and adds the options for the choice box
    public void InitializeProfilePage()
    {
        //initialising Goals List
        GoalList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int selection = GoalList.getSelectionModel().getSelectedIndex();

                if (selection != -1)
                {
                    setSelectedGoal(selection);
                    System.out.println("Updated selected goal to: " + getSelectedGoal());
                }
            }
        });

        try
        {
            this.goals = Main.getDatabase().getGoals(Main.getDatabase().getUsername());

            Collections.sort(this.goals);

            GoalList.getItems().clear();

            for (Goal goal : this.goals)
            {
                GoalList.getItems().add(goal.getDescription());
            }

        }
        catch (SQLException e)
        {
            AlertBox.display("Error", "A problem occurred getting your goals. Please try again later...");
        }
    }

    private void setSelectedGoal(int selectedGoal)
    {
        this.selectedGoal = selectedGoal;
        if (selectedGoal != -1)
        {
            DescriptionLabel.setText(this.goals.get(selectedGoal).getDescription());
            TypeLabel.setText(this.goals.get(selectedGoal).getGoalType().toString());
            TDateLabel.setText(this.goals.get(selectedGoal).getTargetDate().toString());

            switch (this.goals.get(selectedGoal).getGoalType())
            {
                case WEIGHT -> ValueLabel.setText(((WeightGoal)this.goals.get(selectedGoal)).getTargetWeight().toString());
                case DISTANCE -> ValueLabel.setText(((DistanceGoal)this.goals.get(selectedGoal)).getTargetDistance().toString());
                case DURATION -> ValueLabel.setText(((DurationGoal)this.goals.get(selectedGoal)).getTargetDuration().toString());
            }
        }
        else
        {
            DescriptionLabel.setText("");
            TypeLabel.setText("");
            TDateLabel.setText("");
            ValueLabel.setText("");
        }
    }

    private int getSelectedGoal()
    {
        return selectedGoal;
    }
}
