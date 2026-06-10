package edu.curso;

import edu.curso.fronteira.LoginBoundary;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        LoginBoundary login =
                new LoginBoundary(stage);

        Scene scene =
                new Scene(login.render(), 400, 250);

        stage.setTitle("Login - Gestão Comercial");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}