package org.affirmio.affirmio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static org.affirmio.affirmio.HelloApplication.*;

public class HelloController {
    public TextField nameInput, TextwithP;

    public Stage HelloAppStage = HelloApplication.window;

    public PasswordField passInput;
    public CheckBox ShowPass;
    public Button LoginButton, CreateUser;
    public static String AuthenticatedUser;
    public Label InvalidPass, Userexist, Usersuccess, nameLabel, passLabel;


    @FXML
    private Label welcomeText;
    @FXML
    private GridPane dynamicElementsContainer;

    public Scene scene2;

    @FXML
    protected void initialize() {
        InvalidPass.setVisible(false);
        Userexist.setVisible(false);
        Usersuccess.setVisible(false);

        window.setTitle("Affirm.io");
    }

    public void ShowPassword(ActionEvent actionEvent) {
        System.out.println("Show Password");

        if (ShowPass.isSelected()) {
            passInput.setManaged(false);
            passInput.setVisible(false);

            TextwithP = new TextField();
            TextwithP.setPromptText("Enter Password");
            TextwithP.setText(passInput.getText());

            GridPane.setConstraints(TextwithP, 2, 3);
            dynamicElementsContainer.getChildren().add(TextwithP);

        } else {
            passInput.setManaged(true);
            passInput.setVisible(true);

            dynamicElementsContainer.getChildren().remove(TextwithP);
            GridPane.setConstraints(passInput, 2, 3);
        }
    }

    public void AuthenticationOnEnter(KeyEvent e) throws IOException {
        System.out.println("Authentication on Enter");

        if (e.getCode() == KeyCode.ENTER) {
            System.out.println("Authenticate");

            AuthenticatedUser = HelloApplication.AuthenticatedUser(nameInput.getText(), passInput.getText());

            if (AuthenticatedUser != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Motivation-view.fxml"));
                Parent root = loader.load();
                MotivationViewController motivationController = loader.getController();

                motivationController.SetMotivation(AuthenticatedUser);
                //HelloApplication.advanceScene(scene2)

                scene2 = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Motivation-view.fxml"))), 700, 700);

                HelloAppStage.setScene(scene2);
            } else if (!HelloApplication.SQLUsername(nameInput.getText()) || !HelloApplication.SQLPassword(passInput.getText(), nameInput.getText())) {
                System.out.println("Invalid Pass");
                Userexist.setVisible(false);
                Usersuccess.setVisible(false);
                InvalidPass.setVisible(true);
            }
        }
    }

    public void Authentication(ActionEvent actionEvent) throws IOException {
        System.out.println("Authenticate");
        AuthenticatedUser = HelloApplication.AuthenticatedUser(nameInput.getText(), passInput.getText());

        if (AuthenticatedUser != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Motivation-view.fxml"));
            Parent root = loader.load();
            MotivationViewController motivationController = loader.getController();

            motivationController.SetMotivation(AuthenticatedUser);

            //HelloApplication.advanceScene(scene2)
            scene2 = new Scene(root, 400, 400);

            HelloAppStage.setScene(scene2);
        } else if (!HelloApplication.SQLUsername(nameInput.getText()) || !HelloApplication.SQLPassword(passInput.getText(), nameInput.getText())) {
            System.out.println("Invalid Pass");
            Userexist.setVisible(false);
            Usersuccess.setVisible(false);
            InvalidPass.setVisible(true);
        }
        //HelloApplication.Authentication(nameInput.getText(), passInput.getText(), HelloAppStage, HelloApplication.scene2, layout2);

    }

    public void CreateUser(ActionEvent actionEvent) {
        String NewUser = nameInput.getText();

        String NewPass = passInput.getText();

        if (HelloApplication.CreateUser(NewUser, NewPass)) {
            InvalidPass.setVisible(false);
            Userexist.setVisible(false);
            Usersuccess.setVisible(true);
        } else if (!HelloApplication.isValidPassword(NewPass)) {
            InvalidPass.setVisible(true);
            Userexist.setVisible(false);
            Usersuccess.setVisible(false);
        } else {
            InvalidPass.setVisible(false);
            Userexist.setVisible(true);
            Usersuccess.setVisible(false);
        }
    }
}
