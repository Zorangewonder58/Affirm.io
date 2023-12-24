package org.affirmio.affirmio;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class AddMotivationViewController {

    public Button SendMotivation;//Button to send the Motivation info over the Motivation View for adding
    public TextField RecordMotivation;//Text Field to hold the new Motivation user adds

    public String newMotivation;
    public static boolean Set = false;

    public Stage HelloAppStage = HelloApplication.window;



    public void SendMotivation(ActionEvent actionEvent) throws IOException {

       newMotivation = RecordMotivation.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Motivation-view.fxml"));
        Parent root = loader.load();
        MotivationViewController motivationController = loader.getController();

        Stage thiswindow = (Stage) SendMotivation.getScene().getWindow();

        HelloApplication.AddtoDatabase(newMotivation, "affirmations", "Motivations");

        Set = true;

        thiswindow.close();

        return;
    }

    public void Return(ActionEvent actionEvent) {

        Stage thiswindow = (Stage) SendMotivation.getScene().getWindow();

        thiswindow.close();

        return;
    }
}
