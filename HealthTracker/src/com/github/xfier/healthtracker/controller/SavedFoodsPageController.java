package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.FoodDiary;
import com.github.xfier.healthtracker.model.data.Food;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SavedFoodsPageController implements Initializable
{
    @FXML private TableView<Food> SavedFoodsTable = new TableView<>();

    public void DeleteFoodPressed() throws Exception
    {
        if (SavedFoodsTable.getSelectionModel().selectedItemProperty().get() != null)
        {
            if (!Main.getDatabase().removeSavedFood(SavedFoodsTable.getSelectionModel().selectedItemProperty().get().getName(), Main.getDatabase().getUsername()))
            {
                AlertBox.display("Error", "Failed to delete a saved food. If this persists, you are trying to delete a default food, which is not allowed.");
            }
            else
            {
                AlertBox.display("Success", "Deleted the saved food called: " + SavedFoodsTable.getSelectionModel().selectedItemProperty().get().getName() + ". If the food remains in the list, it is a default food and cannot be removed.");
                init();
            }
        }
    }

    public void CreateNewFoodPressed() throws Exception {
        Parent SavedFoodParent = FXMLLoader.load(getClass().getResource("../view/NewFoodWindow.fxml"));
        Scene CreateNewFood = new Scene(SavedFoodParent,400,400);

        Stage window = new Stage();
        window.setScene(CreateNewFood);

        window.setOnCloseRequest(event -> init());

        window.show();
    }

    public void AddToDiary() throws Exception
    {
        if (SavedFoodsTable.getSelectionModel().selectedItemProperty().get() != null)
        {
            if (!Main.getDatabase().addFoodToMeal(SavedFoodsTable.getSelectionModel().selectedItemProperty().get(), Main.getDatabase().getMeal(), Main.getDatabase().getUsername()))
            {
                AlertBox.display("Error", "Failed to add the selected food to the " + Main.getDatabase().getMeal().toString() + " meal.");
            }
            else
            {
                Stage stage = ((Stage) SavedFoodsTable.getScene().getWindow());

                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                stage.close();

                AlertBox.display("Success", "Added the selected food to the " + Main.getDatabase().getMeal().toString() + " meal.");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        init();
    }

    private void init()
    {
        try
        {
            SavedFoodsTable.setItems(FXCollections.observableArrayList(Main.getDatabase().getFoodDiary(Main.getDatabase().getUsername()).getFoodsList()));
        }
        catch (SQLException e)
        {
            AlertBox.display("Error", "Failed to load food diary. Please try to reload the page.");
        }

        SavedFoodsTable.getColumns().get(0).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getName()));
        SavedFoodsTable.getColumns().get(1).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getCalories() + " Calories"));
    }
}
