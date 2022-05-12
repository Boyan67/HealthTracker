package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginPageController{

    @FXML private TextField username = new TextField();
    @FXML private PasswordField password = new PasswordField();
    @FXML private Label errorLabel = new Label();
    @FXML private Button loginButton = new Button();

    // if password box has had text entered
    private boolean passEntered = false;

    // When login is pressed - check fields are not empty and go to home page (no validation)
    public void LogInButtonPushed(javafx.event.ActionEvent event){

        String passwordValue = password.getText();
        String usernameValue = username.getText();

        //TODO Store which user is now using the application.
        if (!(passwordValue.equals("")) && !(usernameValue.equals("")) && Main.getDatabase().loginUser(usernameValue, passwordValue)){

            Parent HomeParent;
            try {
                HomeParent = FXMLLoader.load(getClass().getResource("../view/HomePage.fxml"));
                Scene HomeScene = new Scene(HomeParent,1000,700);

                //this line gets the stage information
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(HomeScene);
                window.show();
            } catch (IOException e) {
                System.out.println("LogIn Button Error: ");
                e.printStackTrace();
            }

        }else{
            errorLabel.setText("Please enter a valid username and password.");
        }
    }

    // Navigate to sign up page
    public void SignUpButtonPushed(javafx.event.ActionEvent event){
        Parent SignUpParent;
        try {
            SignUpParent = FXMLLoader.load(getClass().getResource("../view/SignUpPage.fxml"));
            Scene HomeScene = new Scene(SignUpParent,750,500);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(HomeScene);
            window.show();
        } catch (IOException e) {
            System.out.println("SignUp Button Error: ");
            e.printStackTrace();
        }
    }

    public void ForgotPassword(javafx.event.ActionEvent event)
    {
        AlertBox.display("Forgotten Password", "Please Contact your administrator");
    }

    public void passwordEntered(KeyEvent event)
    {
        // ensure event only fires once
        if (passEntered) { return; }
        passEntered = true;

        // add event listener so login button fires on ENTER
        Node root = password.getScene().getRoot();
        root.addEventHandler(KeyEvent.KEY_PRESSED, e ->
        {
            if (e.getCode() == KeyCode.ENTER) { loginButton.fire(); }
        });
    }
}
