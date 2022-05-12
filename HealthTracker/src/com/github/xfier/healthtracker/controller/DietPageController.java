package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.FoodDiary;
import com.github.xfier.healthtracker.model.data.Food;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DietPageController implements Initializable
{
    private FoodDiary foodDiary;

    @FXML ListView<String> BreakfastList = new ListView<>();
    @FXML ListView<String> LunchList = new ListView<>();
    @FXML ListView<String> DinnerList =  new ListView<>();



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

    public void AddBreakfastPushed() throws Exception
    {
        Parent SavedFoodParent = FXMLLoader.load(getClass().getResource("../view/SavedFoodsPage.fxml"));
        Scene AddBreakfastScene = new Scene(SavedFoodParent,700,500);
        Main.getDatabase().setMeal(FoodDiary.Meal.BREAKFAST);

        Stage window = new Stage();
        window.setOnCloseRequest(event -> init());
        window.setScene(AddBreakfastScene);
        window.show();
    }
    public void AddLunchPushed() throws Exception
    {
        Parent SavedFoodParent = FXMLLoader.load(getClass().getResource("../view/SavedFoodsPage.fxml"));
        Scene AddLunchScene = new Scene(SavedFoodParent,700,500);
        Main.getDatabase().setMeal(FoodDiary.Meal.LUNCH);

        Stage window = new Stage();
        window.setOnCloseRequest(event -> init());
        window.setScene(AddLunchScene);
        window.show();
    }
    public void AddDinnerPushed() throws Exception
    {
        Parent SavedFoodParent = FXMLLoader.load(getClass().getResource("../view/SavedFoodsPage.fxml"));
        Scene AddDinnerScene = new Scene(SavedFoodParent,700,500);
        Main.getDatabase().setMeal(FoodDiary.Meal.DINNER);

        Stage window = new Stage();
        window.setOnCloseRequest(event -> init());
        window.setScene(AddDinnerScene);
        window.show();
    }

    public void RemoveBreakfastFood() throws Exception
    {
        if (BreakfastList.getSelectionModel().getSelectedItems().size() > 0)
        {
            if (Main.getDatabase().removeOneMealFood(FoodDiary.Meal.BREAKFAST, BreakfastList.getSelectionModel().getSelectedItems().get(0), Main.getDatabase().getUsername()))
            {
                AlertBox.display("Success", "Successfully removed " + BreakfastList.getSelectionModel().getSelectedItems().get(0) + " from your breakfast foods.");
                init();
            }
            else
            {
                AlertBox.display("Error", "Failed to remove from your breakfast foods. Please try again later.");
            }
        }
    }

    public void RemoveLunchFood() throws Exception
    {
        if (LunchList.getSelectionModel().getSelectedItems().size() > 0)
        {
            if (Main.getDatabase().removeOneMealFood(FoodDiary.Meal.LUNCH, LunchList.getSelectionModel().getSelectedItems().get(0), Main.getDatabase().getUsername()))
            {
                AlertBox.display("Success", "Successfully removed " + LunchList.getSelectionModel().getSelectedItems().get(0) + " from your lunch foods.");
                init();
            }
            else
            {
                AlertBox.display("Error", "Failed to remove from your lunch foods. Please try again later.");
            }
        }
    }

    public void RemoveDinnerFood() throws Exception
    {
        if (DinnerList.getSelectionModel().getSelectedItems().size() > 0)
        {
            if (Main.getDatabase().removeOneMealFood(FoodDiary.Meal.DINNER, DinnerList.getSelectionModel().getSelectedItems().get(0), Main.getDatabase().getUsername()))
            {
                AlertBox.display("Success", "Successfully removed " + DinnerList.getSelectionModel().getSelectedItems().get(0) + " from your dinner foods.");
                init();
            }
            else
            {
                AlertBox.display("Error", "Failed to remove from your dinner foods. Please try again later.");
            }
        }
    }

    public void clearBreakfast() throws Exception
    {
        if (Main.getDatabase().clearMealFoods(FoodDiary.Meal.BREAKFAST, Main.getDatabase().getUsername()))
        {
            AlertBox.display("Success", "Successfully cleared your breakfast foods.");
            init();
        }
        else
        {
            AlertBox.display("Error", "Failed to clear your breakfast foods. Please try again later.");
        }
    }

    public void clearLunch() throws Exception
    {
        if (Main.getDatabase().clearMealFoods(FoodDiary.Meal.LUNCH, Main.getDatabase().getUsername()))
        {
            AlertBox.display("Success", "Successfully cleared your lunch foods.");
            init();
        }
        else
        {
            AlertBox.display("Error", "Failed to clear your lunch foods. Please try again later.");
        }
    }

    public void clearDinner() throws Exception
    {
        if (Main.getDatabase().clearMealFoods(FoodDiary.Meal.DINNER, Main.getDatabase().getUsername()))
        {
            AlertBox.display("Success", "Successfully cleared your dinner foods.");
            init();
        }
        else
        {
            AlertBox.display("Error", "Failed to clear your dinner foods. Please try again later.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
    }

    public void init()
    {
        try
        {
            this.foodDiary = Main.getDatabase().getFoodDiary(Main.getDatabase().getUsername());
        }
        catch (SQLException e)
        {
            AlertBox.display("Error", "Failed to load  your food diary. Please try again");
        }

        BreakfastList.getItems().clear();
        LunchList.getItems().clear();
        DinnerList.getItems().clear();

        for (Food food : this.foodDiary.getMeal(FoodDiary.Meal.BREAKFAST))
        {
            BreakfastList.getItems().add(food.getName());
        }
        for (Food food : this.foodDiary.getMeal(FoodDiary.Meal.LUNCH))
        {
            LunchList.getItems().add(food.getName());
        }
        for (Food food : this.foodDiary.getMeal(FoodDiary.Meal.DINNER))
        {
            DinnerList.getItems().add(food.getName());
        }
    }
}
