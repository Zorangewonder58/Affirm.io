package org.affirmio.affirmio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static org.affirmio.affirmio.HelloApplication.*;
import static org.affirmio.affirmio.HelloApplication.Motivation;

public class MotivationViewController {


    public Label Motivation, Greeting, Precursor;
    public Stage HelloAppStage = window;
    @FXML
    public Label Response;
    Scene Login;

    @FXML
    protected void initialize()
    {
        Response.setVisible(false);
    }

    public void SetGreeting(String AuthenticatedUser)
    {
        Greeting.setText(Greeting.getText() + ", " + AuthenticatedUser);
    }

    public void SetMotivation(String AuthenticatedUser)
    {
        Precursor.setText("\n Here is your Motivation");
        Precursor.setStyle("-fx-font-weight: bold");

        SetGreeting(AuthenticatedUser);
        Motivation.setText(Motivation());
        System.out.println(Motivation.getText());

    }

    public void MotivationAdd(ActionEvent actionEvent) throws IOException {
        //Need to change FileInsert Me

        //Section for Adding Motivation
        //Create a pop up window
        Stage newwindow = new Stage();

        newwindow.initModality(Modality.APPLICATION_MODAL);

        newwindow.setTitle("Motivation Add");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMotivation-view.fxml"));
        Parent Motivationadding = loader.load();
        AddMotivationViewController controller = loader.getController();

        Scene MotivationAdding = new Scene(Motivationadding, 300, 200);

        //Scene must communicate to send Motivation

        newwindow.setScene(MotivationAdding);

        newwindow.showAndWait();

        Response.setVisible(Show(AddMotivationViewController.Set));
    }

    public void LoginReturn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent loginscreen = loader.load();


        Login = new Scene(loginscreen, 400, 400);
        HelloAppStage.setScene(Login);
    }

    public static boolean Show(boolean show)
    {
        return show;
    }
}
