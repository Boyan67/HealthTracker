package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpPageController {

    @FXML private TextField username = new TextField();
    @FXML private TextField email = new TextField();
    @FXML private PasswordField password = new PasswordField();
    @FXML private PasswordField passwordConfirm = new PasswordField();
    @FXML private Label errorLabel = new Label();

    // Checks password format
    public static boolean isValidPassword(String password) {
        String regex =  "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    // Checks email format
    public static boolean isValidEmail(String email) {
        String regex =  "^(?=\\S{0,254}$)[\\w.-]{0,63}[a-z0-9]@[a-z0-9-]+\\.[a-z0-9-]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    // Checks username format
    public static boolean isValidUsername(String username) {
        String regex =  "^[a-zA-Z][a-zA-Z0-9_]{6,19}$";
        return username.matches(regex);
    }
    // Checks passwords match
    public static boolean confirmPasswordMatches(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    // Navigates back to log in screen
    public void LogInScreen(javafx.event.ActionEvent event){
        Parent HomeParent;
        try {
            HomeParent = FXMLLoader.load(getClass().getResource("../view/LoginPage.fxml"));
            Scene HomeScene = new Scene(HomeParent,750,500);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(HomeScene);
            window.show();
        } catch (IOException e) {
            System.out.println("Back to Log In Button Error: " );
            e.printStackTrace();
        }
    }

    // Checks all fields are valid and displays appropriate error messages
    public void SignUpButtonPushed(javafx.event.ActionEvent event){
        String invalidPassword = "Password must be 8-20 characters, include at least 1 number, 1 capital letter, 1 lowercase letter. ";
        String invalidConfirm = "Passwords don't match. ";
        String invalidEmail = "Please enter a valid email. ";
        String invalidUserName = "Please enter a valid username";
        String fullError = "";

        if (isValidPassword(password.getText()) &&
                isValidEmail(email.getText()) &&
                isValidUsername(username.getText()) &&
                confirmPasswordMatches(password.getText(), passwordConfirm.getText())
        ){
            Database database = Main.getDatabase();

            try
            {
                if (database.registerUser(username.getText(), email.getText(), password.getText()))
                {
                    errorLabel.setText("Successfully Registered you can now log in");
                }
                else
                {
                    errorLabel.setText("Failed to register. Please try again.");
                }
            }
            catch (IllegalArgumentException e)
            {
                errorLabel.setText(e.getMessage());
            }
        }else{
            if (!isValidPassword(password.getText())){
                fullError += invalidPassword + "\n";
            }
            if (!isValidEmail(email.getText())){
                fullError += invalidEmail + "\n";
            }
            if (!isValidUsername(username.getText())) {
                fullError += invalidUserName + "\n";
            }
            if (!confirmPasswordMatches(password.getText(), passwordConfirm.getText())) {
                fullError += invalidConfirm + "\n";
            }
            errorLabel.setText(fullError);
        }

    }
}
