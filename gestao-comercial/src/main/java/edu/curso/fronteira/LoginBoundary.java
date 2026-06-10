package edu.curso.fronteira;

import edu.curso.controle.LoginControl;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginBoundary {

    private Stage stage;
    private LoginControl control = new LoginControl();

    private TextField txtLogin = new TextField();
    private PasswordField txtSenha = new PasswordField();
    private Label lblMensagem = new Label();

    public LoginBoundary(Stage stage) {
        this.stage = stage;
    }

    public Parent render() {
        GridPane painel = new GridPane();

        painel.setPadding(new Insets(20));
        painel.setHgap(10);
        painel.setVgap(10);

        Button btnEntrar = new Button("Entrar");

        painel.add(new Label("Login:"), 0, 0);
        painel.add(txtLogin, 1, 0);

        painel.add(new Label("Senha:"), 0, 1);
        painel.add(txtSenha, 1, 1);

        painel.add(btnEntrar, 1, 2);
        painel.add(lblMensagem, 1, 3);

        btnEntrar.setOnAction(e -> entrar());

        return painel;
    }

    private void entrar() {
        String login = txtLogin.getText();
        String senha = txtSenha.getText();

        boolean autenticado =
                control.autenticar(login, senha);

        if (autenticado) {
            PrincipalBoundary principal =
                    new PrincipalBoundary(stage);

            Scene scene =
                    new Scene(principal.render(), 900, 600);

            stage.setTitle("Gestão Comercial");
            stage.setScene(scene);
        } else {
            lblMensagem.setText("Login ou senha inválidos");
        }
    }
}