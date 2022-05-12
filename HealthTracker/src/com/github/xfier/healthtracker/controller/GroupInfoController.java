package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.model.Goal;
import com.github.xfier.healthtracker.model.User;
import com.github.xfier.healthtracker.model.group.Group;
import javafx.fxml.Initializable;
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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.text.TabableView;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GroupInfoController implements Initializable {

    //Configuration of group members table
    @FXML
    private TableView<User> GroupInfo = new TableView<>();

    @FXML private Label Title = new Label();

    private Group group;

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
        Scene ExerciseScene = new Scene(DietParent, 1000, 700);

        //this line gets the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ExerciseScene);
        window.show();
    }

    public void GoalsButtonPushed(javafx.event.ActionEvent event) throws Exception
    {

        Parent GoalsParent = FXMLLoader.load(getClass().getResource("../view/GoalsPage.fxml"));
        Scene ProfileScene = new Scene(GoalsParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(ProfileScene);
        window.show();
    }

    //method for leave group button
    public void LeaveGroupPushed(javafx.event.ActionEvent event) throws Exception
    {
        if (this.group.getLeader().getUsername().equals(Main.getDatabase().getUsername()))
        {
            AlertBox.display("Error", "You can't leave a group that you're the leader of!");
            return;
        }

        if (!this.group.getMembers().stream().map(user -> user.getUsername()).collect(Collectors.toList()).contains(Main.getDatabase().getUsername()))
        {
            AlertBox.display("Error", "You can't leave a group that you're not a member of!");
            return;
        }

        if(Main.getDatabase().removeMemberFromGroup(this.group.getId(), Main.getDatabase().getUsername()))
        {
            AlertBox.display("Success", "Successfully removed from group.");
            InitializeHomePage();
            Stage stage = ((Stage) GroupInfo.getScene().getWindow());

            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_SHOWN)); // Not the correct event, but we can listen to this and refresh the other window
        }
        else
        {
            AlertBox.display("Error", "Failed to remove you from the group. Please try again.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitializeHomePage();
    }

    // initialises values in the profile page, loading a user and adds the options for the choice box
    public void InitializeHomePage() {
        //populating group members table with data from database
        try{
            if (this.group == null)
            {
                this.group = Main.getDatabase().getGroup(Main.getDatabase().getTargetGroupId());
            }
            else
            {
                this.group = Main.getDatabase().getGroup(this.group.getId());
            }

            GroupInfo.setItems(FXCollections.observableArrayList(this.group.getMembers()));
            Title.setText(this.group.getName() + " Group Members");
        }
        catch (SQLException e ){
            AlertBox.display("Error", "Failed to get group information, please reload page to try again.");
        }

        GroupInfo.getColumns().get(0).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getUsername()));
        GroupInfo.getColumns().get(1).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getForename()));
        GroupInfo.getColumns().get(2).setCellValueFactory(param -> new
                ReadOnlyObjectWrapper(param.getValue().getSurname()));
    }


}

