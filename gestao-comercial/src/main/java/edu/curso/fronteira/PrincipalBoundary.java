package edu.curso.fronteira;

import edu.curso.seguranca.Sessao;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrincipalBoundary {

    private Stage stage;

    public PrincipalBoundary(Stage stage) {
        this.stage = stage;
    }

    public Parent render() {
        BorderPane painel = new BorderPane();

        MenuBar menuBar = new MenuBar();

        Menu menuCadastros = new Menu("Cadastros");

        MenuItem itemClientes = new MenuItem("Clientes");
        MenuItem itemProdutos = new MenuItem("Produtos");
        MenuItem itemPedidos = new MenuItem("Pedidos");

        String perfil = Sessao.getUsuarioLogado().getPerfil();

        if (perfil.equals("VENDEDOR")) {
            itemProdutos.setDisable(true);
        }

        itemClientes.setOnAction(e -> {
            ClienteBoundary tela = new ClienteBoundary();
            painel.setCenter(tela.render());
        });

        itemProdutos.setOnAction(e -> {
            ProdutoBoundary tela = new ProdutoBoundary();
            painel.setCenter(tela.render());
        });

        itemPedidos.setOnAction(e -> {
            PedidoBoundary tela = new PedidoBoundary();
            painel.setCenter(tela.render());
        });

        menuCadastros.getItems().addAll(
                itemClientes,
                itemProdutos,
                itemPedidos
        );

        menuBar.getMenus().add(menuCadastros);

        Label lblBoasVindas = new Label(
                "Usuário logado: " +
                        Sessao.getUsuarioLogado().getLogin() +
                        " | Perfil: " +
                        Sessao.getUsuarioLogado().getPerfil()
        );

        painel.setTop(menuBar);
        painel.setCenter(lblBoasVindas);

        return painel;
    }
}