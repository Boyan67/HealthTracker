package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.DistanceGoal;
import com.github.xfier.healthtracker.model.DurationGoal;
import com.github.xfier.healthtracker.model.Goal;
import com.github.xfier.healthtracker.model.Goal;
import com.github.xfier.healthtracker.model.Goal;

import com.github.xfier.healthtracker.model.WeightGoal;
import com.github.xfier.healthtracker.model.data.Distance;
import com.github.xfier.healthtracker.model.data.Weight;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class AddGoalPageController implements Initializable {


    @FXML private ChoiceBox GoalType = new ChoiceBox();//-------ChoiceBoxes
    @FXML private TextField GoalValue = new TextField();//--------TextFields
    @FXML private DatePicker GoalDate = new DatePicker();
    @FXML private TextArea GoalDescription = new TextArea();

    public void SetGoalPushed() throws Exception
    {
        try
        {
            if (GoalDescription.getText().isEmpty())
            {
                AlertBox.display("Error", "All goal fields are required!");
                return;
            }

            boolean success = switch (GoalType.getValue().toString())
                {
                    case "Weight Goal" -> Main.getDatabase().addGoal(new WeightGoal(GoalDate.getValue(), GoalDescription.getText(), new Weight(Integer.parseInt(GoalValue.getText()))), Main.getDatabase().getUsername());
                    case "Distance Goal" -> Main.getDatabase().addGoal(new DistanceGoal(GoalDate.getValue(), GoalDescription.getText(), new Distance(Integer.parseInt(GoalValue.getText()))), Main.getDatabase().getUsername());
                    //TODO Update this to be more user friendly
                    case "Duration Goal" -> Main.getDatabase().addGoal(new DurationGoal(GoalDate.getValue(), GoalDescription.getText(), Duration.parse(GoalValue.getText())), Main.getDatabase().getUsername());
                    default -> false;
                };

            if (!success)
            {
                AlertBox.display("Error", "Failed to add the goal. Please try again later...");
            }
            else
            {
                Stage stage = ((Stage) GoalType.getScene().getWindow());

                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                stage.close();

                AlertBox.display("Success", "You have successfully added the goal.");

            }
        }
        catch (NumberFormatException e)
        {
            AlertBox.display("Error", "Goal Value must be an integer!");
        }
        catch (DateTimeParseException | IllegalArgumentException e)
        {
            AlertBox.display("Error", e.getMessage());
        }
        catch (NullPointerException e)
        {
            AlertBox.display("Error", "All goal fields are required!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InitializeAddGoalPage();
    }

    public void InitializeAddGoalPage()
    {
        //Initial setup
        Tooltip tooltip = new Tooltip("Enter Time in the Format \"PT20M\" - (M represents minutes)");
        GoalValue.setTooltip(tooltip);
        GoalType.getItems().addAll("Weight Goal", "Distance Goal", "Duration Goal");
        GoalType.setValue("Weight Goal");
    }
}
