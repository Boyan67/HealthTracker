package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.text.TabableView;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GroupController implements Initializable {

    //configuration of the group tables
    @FXML
    private TableView<com.github.xfier.healthtracker.model.group.Group> MyGroups = new TableView<>();
    @FXML
    private TableView<com.github.xfier.healthtracker.model.group.Group> AllGroups = new TableView<>();
    @FXML
    private TableColumn<Group, String> groupNameColumn;
    @FXML
    private TableColumn<Group, String> adminNameColumn;

    @FXML
    TextField GroupName = new TextField();


    /* All Controllers need the functionality to be able to swap between pages,
       so these 3 button events will be in each of the controllers.
     */

    public void HomeButtonPushed(javafx.event.ActionEvent event) throws Exception {
        Parent HomeParent = FXMLLoader.load(getClass().getResource("../view/HomePage.fxml"));
        Scene HomeScene = new Scene(HomeParent, 1000, 700);

        //this line gets the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(HomeScene);
        window.show();

    }

    public void ProfileButtonPushed(javafx.event.ActionEvent event) throws Exception {

        Parent ProfileParent = FXMLLoader.load(getClass().getResource("../view/ProfilePage.fxml"));
        Scene ProfileScene = new Scene(ProfileParent, 1000, 700);

        //this line gets the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ProfileScene);
        window.show();

    }

    public void ExerciseButtonPushed(javafx.event.ActionEvent event) throws Exception {

        Parent ExerciseParent = FXMLLoader.load(getClass().getResource("../view/ExercisePage.fxml"));
        Scene ExerciseScene = new Scene(ExerciseParent, 1000, 700);

        //this line gets the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ExerciseScene);
        window.show();
    }

    public void DietButtonPushed(javafx.event.ActionEvent event) throws Exception {
        Parent DietParent = FXMLLoader.load(getClass().getResource("../view/DietPage.fxml"));
        Scene DietScene = new Scene(DietParent, 1000, 700);

        //this line gets the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
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


    //method to open new window when 'View Selected' button is pushed within UI
    public void ViewSelectedPushed(javafx.event.ActionEvent event) throws Exception
    {
        if(AllGroups.getSelectionModel().getSelectedItems().size() > 0){
            Main.getDatabase().setTargetGroupId(AllGroups.getSelectionModel().getSelectedItems().get(0).getId());

            Parent GroupInfoParent = FXMLLoader.load(getClass().getResource("../view/GroupInfoPage.fxml"));
            Scene GroupInfoScene = new Scene(GroupInfoParent, 700, 500);
            //this line gets the stage information
            Stage window = new Stage();

            window.setOnShown(e ->
            {
                InitializeHomePage();
            });

            window.setScene(GroupInfoScene);
            window.show();
        }
    }

    //method for join group button
    public void JoinSelectedPushed(javafx.event.ActionEvent event) throws Exception
    {
        if(AllGroups.getSelectionModel().getSelectedItems().size() > 0){
            try
            {
                if(Main.getDatabase().addMemberToGroup(AllGroups.getSelectionModel().getSelectedItems().get(0).getId(), Main.getDatabase().getUsername()))
                {
                    AlertBox.display("Success", "You have joint the group.");
                    InitializeHomePage();
                }
                else{
                    AlertBox.display("Error", "Failed to join group. Please try again later");
                }
            }
            catch (IllegalArgumentException e)
            {
                AlertBox.display("Error", e.getMessage());
            }
        }
    }
    public void CreateGroupPushed(javafx.event.ActionEvent event) throws Exception
    {
        if (GroupName.getText().isEmpty())
        {
            return;
        }
        else
        {
            if (Main.getDatabase().addGroup(new com.github.xfier.healthtracker.model.group.Group(GroupName.getText(),
                Main.getDatabase().getUser(Main.getDatabase().getUsername()))))
            {
                AlertBox.display("Success", "Successfully created your group!");
                InitializeHomePage();
            }
            else
            {
                AlertBox.display("Error", "Failed to create the group. Please try again.");
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        InitializeHomePage();
    }

    // initialises values in the profile page, loading a user and adds the options for the choice box
    public void InitializeHomePage() {

        //populating your groups table with data from database
        try{
            MyGroups.setItems(FXCollections.observableArrayList(Main.getDatabase().getGroups(Main.getDatabase().getUsername())));
        }
        catch (SQLException e ){
            AlertBox.display("Error", "Failed to get your groups, please reload page to try again.");
        }

        MyGroups.getColumns().get(0).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getName()));
        MyGroups.getColumns().get(1).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getLeader().getUsername()));

        //populating all groups table with data from database
        try{
            AllGroups.setItems((FXCollections.observableArrayList(Main.getDatabase().getAllGroups())));
        }
        catch (SQLException e){
            AlertBox.display("Error", "Failed to get all groups, please reload page to try again.");
        }

        AllGroups.getColumns().get(0).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getId()));
        AllGroups.getColumns().get(1).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getName()));
        AllGroups.getColumns().get(2).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getLeader().getUsername()));



    }


}
