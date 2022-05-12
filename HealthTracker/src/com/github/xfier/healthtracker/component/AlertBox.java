package com.github.xfier.healthtracker.component;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox
{


    public static void display(String title, String message)
    {


        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(100);

        Label l1 = new Label();
        l1.setText(message);

        Button b1= new Button("Ok");
        b1.setOnAction(e -> window.close());


        VBox layout = new VBox(10);
        layout.getChildren().addAll(l1, b1);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


    }
}