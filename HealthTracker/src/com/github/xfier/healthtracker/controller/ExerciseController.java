package com.github.xfier.healthtracker.controller;


import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.DistanceExercise;
import com.github.xfier.healthtracker.model.Exercise;
import com.github.xfier.healthtracker.model.WeightExercise;
import com.github.xfier.healthtracker.model.data.Distance;
import com.github.xfier.healthtracker.model.data.Weight;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ExerciseController implements Initializable{

    //list options for ExerciseType combo box
    ObservableList<String> ExerciseType = FXCollections.observableArrayList(Arrays.stream(Exercise.ExerciseType.values()).map(type -> type.toString()).collect(Collectors.toList()));


    @FXML private ComboBox<String> ExerciseTypeBox = new ComboBox<>();
    @FXML private TextArea ExerciseDescription = new TextArea();
    @FXML private TextField ExerciseName = new TextField();
    @FXML private TextField ExerciseValue = new TextField();
    @FXML private TextField ExerciseDuration = new TextField();
    @FXML private TableView<Exercise> YourExercises = new TableView<>();
    @FXML private DatePicker ExerciseDatePicker = new DatePicker();
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

    public void GoalsButtonPushed(javafx.event.ActionEvent event) throws Exception
    {

        Parent GoalsParent = FXMLLoader.load(getClass().getResource("../view/GoalsPage.fxml"));
        Scene GoalsScene = new Scene(GoalsParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(GoalsScene);
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

    //Save and Cancel buttons
    //save button to save data inputted to the database
//    public void SaveButtonPushed(javafx.event.ActionEvent event) throws Exception
//    {
//        Parent SaveParent = FXMLLoader.load(getClass().getResource(""));
//        Scene ExerciseScene = new Scene(ExerciseParent,750,500);
//
//        //this line gets the stage information
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene(ExerciseScene);
//        window.show();
//    }

    //cancel button to clear information inputted
    public void CancelButtonPushed(javafx.event.ActionEvent event) throws Exception
    {


        Parent CancelParent = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Scene CancelScene = new Scene(CancelParent,750,500);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(CancelScene);
        window.show();
    }


    public void SaveExerciseButtonPushed(javafx.event.ActionEvent event) throws Exception
    {
        boolean success = false;

        try
        {
            if (ExerciseName.getText().isEmpty() || ExerciseDescription.getText().isEmpty())
            {
                AlertBox.display("Error", "The name and description are required!");
                return;
            }
            success = switch (Exercise.ExerciseType.valueOf(ExerciseTypeBox.getValue()))
                    {
                        case WEIGHT -> Main.getDatabase().addExercise(new WeightExercise(
                                Duration.parse(ExerciseDuration.getText()),

                                new Weight(Integer.parseInt(ExerciseValue.getText())),
                                ExerciseName.getText(),
                                ExerciseDescription.getText(),
                                ExerciseDatePicker.getValue()), Main.getDatabase().getUsername());
                        case RIDING, RUNNING, WALKING, SWIMMING, CROSSFIT -> Main.getDatabase().addExercise(new DistanceExercise(
                                Exercise.ExerciseType.valueOf(ExerciseTypeBox.getValue()),
                                Duration.parse(ExerciseDuration.getText()),
                                new Distance(Integer.parseInt(ExerciseValue.getText())),
                                ExerciseName.getText(),
                                ExerciseDescription.getText(),
                                ExerciseDatePicker.getValue()), Main.getDatabase().getUsername());
                        default -> false;
                    };
        }
        catch (NumberFormatException e)
        {
            AlertBox.display("Error", "The value must be a valid number.");
            return;
        }
        catch (DateTimeParseException e)
        {
            AlertBox.display("Error", "Please input a valid date and duration.");
            return;
        }
        catch (IllegalArgumentException e)
        {
            if (ExerciseTypeBox.getValue().equals("Select Exercise Type"))
            {
                AlertBox.display("Error", "Please select and exercise type.");
            }
            else
            {
                AlertBox.display("Error", e.getMessage());
            }

            return;
        }
        catch (NullPointerException e)
        {
            AlertBox.display("Error", "All fields are required to add an exercise.");
            return;
        }

        if (!success)
        {
            AlertBox.display("Error", "Failed to save your exercise, please try again.");
        }
        else
        {
            AlertBox.display("Success", "Your exercise has been recorded!");
            InitializeHomePage();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InitializeHomePage();

        //initialise and config of the combo box

        ExerciseTypeBox.setItems(ExerciseType);
        ExerciseTypeBox.setValue("Select Exercise Type");
        Tooltip tooltip = new Tooltip("Enter Time in the Format \"PT20M\" - (M represents minutes)");
        ExerciseDuration.setTooltip(tooltip);


    }

    // initialises values in the profile page, loading a user and adds the options for the choice box
    public void InitializeHomePage()
    {
        try
        {
            YourExercises.setItems(FXCollections.observableArrayList(Main.getDatabase().getExercises(Main.getDatabase().getUsername())));
        }
        catch (SQLException e)
        {
            AlertBox.display("Error", "Failed to get your exercises, please reload the page to try again.");
        }

        YourExercises.getColumns().get(0).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getType()));
        YourExercises.getColumns().get(1).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getDurationLength().toMinutes() + " Minutes"));
        YourExercises.getColumns().get(2).setCellValueFactory(param -> switch (param.getValue().getType())
            {
                case WEIGHT -> new ReadOnlyObjectWrapper(((WeightExercise) param.getValue()).getWeight().toString());
                default -> new ReadOnlyObjectWrapper(((DistanceExercise) param.getValue()).getDistance().toString());
            });
        YourExercises.getColumns().get(3).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getDate().toString()));
    }
}
