package com.github.xfier.healthtracker;

import com.github.xfier.healthtracker.database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main extends Application {

    private static Database database;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.database = new Database();

        Parent root = FXMLLoader.load(getClass().getResource("view/LoginPage.fxml"));
        primaryStage.setTitle("HealthTracker");
        Scene scene = new Scene(root,750,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Database getDatabase()
    {
        return database;
    }
}
