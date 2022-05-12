package com.github.xfier.healthtracker.controller;

import com.github.xfier.healthtracker.Main;
import com.github.xfier.healthtracker.component.AlertBox;
import com.github.xfier.healthtracker.model.DistanceGoal;
import com.github.xfier.healthtracker.model.DurationGoal;
import com.github.xfier.healthtracker.model.Goal;
import com.github.xfier.healthtracker.model.User;
import com.github.xfier.healthtracker.model.WeightGoal;
import com.github.xfier.healthtracker.model.data.Email;
import com.github.xfier.healthtracker.model.data.Height;
import com.github.xfier.healthtracker.model.data.Weight;
import com.github.xfier.healthtracker.model.group.Group;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProfileController implements Initializable{


    //------------------Profile Page--------------------

    // User user = new User ("John Bain", 20, 78.0, 180.0, 75.0);
    User user = new User.Builder("EXAMPLE USER")
            .withForename("John")
            .withSurname("Bain")
            .withDateOfBirth(LocalDate.of(2001, 1, 1))
            .withWeight(new Weight(78))
            .withHeight(new Height(180))
            .withTargetWeight(new WeightGoal(LocalDate.of(2022, 1, 1), "Test", new Weight(75)))
            .withEmail(new Email("jb@example.com"))
            .build();

    @FXML private TextField UserInfo = new TextField();//--------TextFields

    @FXML private ChoiceBox choiceBox = new ChoiceBox();//-------ChoiceBoxes

    @FXML private TableView<Group> GroupTable = new TableView<>();

    @FXML private TableView<Goal> GoalsTable = new TableView<>();

    @FXML private  Button Home, Profile, Exercise , Update; //-----Buttons


    @FXML private Label nameLabel = new Label();//----------------Labels
    @FXML private Label DOBLabel = new Label();
    @FXML private Label weightLabel = new Label();
    @FXML private Label heightLabel = new Label();
    @FXML private Label GoalWeightLabel = new Label();
    @FXML private Label BMILabel = new Label();
    @FXML private Label emailLabel = new Label();
    @FXML private Label userNameLabel = new Label();

    public void HomeButtonPushed(javafx.event.ActionEvent event) throws Exception
    {
        Parent HomeParent = FXMLLoader.load(getClass().getResource("../view/HomePage.fxml"));
        Scene HomeScene = new Scene(HomeParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(HomeScene);
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
    public void DietButtonPushed(javafx.event.ActionEvent event) throws Exception
    {
        Parent DietParent = FXMLLoader.load(getClass().getResource("../view/DietPage.fxml"));
        Scene DietScene = new Scene(DietParent,1000,700);

        //this line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(DietScene);
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


    //This method is connected to the button Update, It updates whatever field is selected in the choice ->
    //box and sets the user.attribute to whatever is in the text field next to it
    public void UpdatePersonalInfo()
    {
        String choice = choiceBox.getValue().toString();
        String UserInput = UserInfo.getText();

        switch (choice) {
            case "Name" -> {
                try {
                    System.out.println(UserInput);
                    if (!UserInput.equals(""))
                    {
                        // FIXME: name requires separate first & last - this solution might work
                        String[] names = UserInput.split(" ");
                        if (names.length != 2) { throw new IllegalArgumentException("First & last name required"); }
                        Main.getDatabase().updateForeAndSurnames(names[0], names[1], Main.getDatabase().getUsername());
                        user.setForename(names[0]);
                        user.setSurname(names[1]);
                        nameLabel.setText(UserInput);
                    }
                }
                catch (SQLException e)
                {
                    AlertBox.display("Error ",
                            "Failed to update your forename and surname, please try again.");
                }
                catch(Exception invalidInfo)
                {
                    AlertBox.display("Invalid Information ",
                            "Info entered must be of type String");
                }
            }
            case "DOB" -> {
                try {
                    System.out.println("Date of Birth " + UserInput);
                    LocalDate date = LocalDate.parse(UserInput);
                    Main.getDatabase().setDateOfBirth(date, Main.getDatabase().getUsername());
                    user.setDateOfBirth(date);
                    DOBLabel.setText(UserInput);
                }
                catch(DateTimeException dateTimeException)
                {
                    AlertBox.display("Invalid Information ",
                            "Please enter your Date of Birth  in the format: YYYY-MM-DD");
                }
                catch (SQLException sqlException)
                {
                    AlertBox.display("Error ", "Failed to update your date of birth, please try again");
                }
            }
            case "Weight" -> {
                try {
                    System.out.println("Weight updated to " + UserInput);
                    int weight = Integer.parseInt(UserInput);
                    Main.getDatabase().setWeight(weight, Main.getDatabase().getUsername());
                    user.setWeight(new Weight(weight));
                    BMILabel.setText(String.valueOf(user.getBMI()));
                    weightLabel.setText(user.getWeight().toString());
                }
                catch(NumberFormatException invalidInfo)
                {
                    AlertBox.display("Invalid Information ",
                            "Info entered must be of type Double");
                }
                catch (SQLException e)
                {
                    AlertBox.display("Error ", "Failed to update your weight, please try again");
                }
            }
            case "Height" -> {
                try {
                    System.out.println("Height updated to " + UserInput);
                    int height = Integer.parseInt(UserInput);
                    Main.getDatabase().setHeight(height, Main.getDatabase().getUsername());
                    user.setHeight(new Height(height));
                    BMILabel.setText(String.valueOf(user.getBMI()));
                    heightLabel.setText(user.getHeight().toString());
                }
                catch(NumberFormatException invalidInfo)
                {
                    AlertBox.display("Invalid Information ",
                            "Info entered must be of type Double");
                }
                catch (SQLException e)
                {
                    AlertBox.display("Error ", "Failed to update your height, please try again.");
                }
            }
            case "Goal Weight" -> {
                try{
                    // FIXME: goal requires date & description (Brandon's note: This isn't finished, not adding database support in it's current state)
                    System.out.println("Goal Weight updated to " + UserInput);
                    GoalWeightLabel.setText(UserInput + "kg");
                }catch(Exception invalidInfo)
                {
                    AlertBox.display("Invalid Information ",
                            "Info entered must be of type Double");
                }
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InitializeProfilePage();
    }

    // initialises values in the profile page, loading a user and adds the options for the choice box
    public void InitializeProfilePage()
    {

        //initialising user data table
        try
        {
            this.user = Main.getDatabase().getUser(Main.getDatabase().getUsername());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        nameLabel.setText(user.getForename() + " " + user.getSurname());
        DOBLabel.setText(String.valueOf(user.getDateOfBirth()));
        weightLabel.setText(String.valueOf(user.getWeight()));
        heightLabel.setText(String.valueOf(user.getHeight()));
        GoalWeightLabel.setText("BELOW");
        BMILabel.setText(String.valueOf(user.getBMI()));
        emailLabel.setText(String.valueOf(user.getEmail()));
        userNameLabel.setText(String.valueOf(user.getUsername()));
        //initialising choice box

        try
        {
            GroupTable.setItems(FXCollections.observableArrayList(Main.getDatabase().getGroups(Main.getDatabase().getUsername())));
        }
        catch (SQLException e)
        {
            AlertBox.display("Error", "Failed to load your groups. Please reload the page and try again.");
        }

        GroupTable.getColumns().get(0).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getName()));
        GroupTable.getColumns().get(1).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getLeader().getUsername()));
        GroupTable.getColumns().get(2).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getMembers().size()));

        choiceBox.getItems().addAll("Name", "DOB", "Weight", "Height");
        //TODO Update goals in here, add support for other goals?
        choiceBox.setValue("Name");

        try
        {
            GoalsTable.setItems(FXCollections.observableArrayList(Main.getDatabase().getGoals(Main.getDatabase().getUsername())));
        }
        catch (SQLException e)
        {
            AlertBox.display("Error", "Failed to load your goals. Please reload the page and try again.");
        }

        GoalsTable.getColumns().get(0).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getDescription()));
        GoalsTable.getColumns().get(1).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getGoalType().toString()));
        GoalsTable.getColumns().get(2).setCellValueFactory(param -> switch (param.getValue().getGoalType()) {
                case WEIGHT -> new ReadOnlyObjectWrapper(((WeightGoal) param.getValue()).getTargetWeight().toString());
                case DURATION -> new ReadOnlyObjectWrapper(((DurationGoal) param.getValue()).getTargetDuration().toMinutes() + " Minutes");
                case DISTANCE -> new ReadOnlyObjectWrapper(((DistanceGoal) param.getValue()).getTargetDistance().toString());
            });
        GoalsTable.getColumns().get(3).setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getTargetDate().toString()));
    }


    /* MOVED TO USER CLASS
    private Double CalculateBMI(Double Weight, Double Height)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        Double BMI = Weight/((Height/100)*(Height/100));
        BMI = Double.parseDouble(df.format(BMI));

        return BMI;
    }*/
}
