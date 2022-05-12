package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.data.Food;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class NewFoodWindowController {

    @FXML TextField FoodName = new TextField();
    @FXML TextField FoodCalories = new TextField();

    public void CreateFoodButton (javafx.event.ActionEvent event) throws Exception
    {
        if (FoodName.getText().isEmpty() || FoodCalories.getText().isEmpty())
        {
            AlertBox.display("Error", "Food name and calories are both required.");
            return;
        }

        try
        {
            Food food = new Food(FoodName.getText(), Integer.parseInt(FoodCalories.getText()));

            if (!Main.getDatabase().createSavedFood(food, Main.getDatabase().getUsername()))
            {
                AlertBox.display("Error", "Failed to save the food. Please try again later");
            }
            else
            {
                AlertBox.display("Success", "Added the food successfully.");
                Stage stage = ((Stage) FoodName.getScene().getWindow());

                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                stage.close();
            }
        }
        catch (NumberFormatException e)
        {
            AlertBox.display("Error", "Calories must be a number.");
            return;
        }
        catch (IllegalArgumentException e)
        {
            AlertBox.display("Error", e.getMessage());
            return;
        }
        catch (NullPointerException e)
        {
            AlertBox.display("Error", "Food name and calories are both required.");
            return;
        }
    }


}
