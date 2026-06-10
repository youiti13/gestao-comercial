package edu.curso.fronteira;

import edu.curso.controle.ClienteControl;
import edu.curso.entidade.Cliente;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ClienteBoundary {

    private ClienteControl control = new ClienteControl();

    private Cliente clienteSelecionado = new Cliente();

    private TextField txtPesquisa = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtCpf = new TextField();
    private TextField txtTelefone = new TextField();
    private TextField txtEmail = new TextField();
    private TextField txtEndereco = new TextField();
    private CheckBox chkAtivo = new CheckBox("Ativo");

    private TableView<Cliente> tabela = new TableView<>();

    public Parent render() {
        BorderPane painelPrincipal = new BorderPane();

        GridPane painelFormulario = new GridPane();
        painelFormulario.setPadding(new Insets(15));
        painelFormulario.setHgap(10);
        painelFormulario.setVgap(10);

        Button btnPesquisar = new Button("Pesquisar");
        Button btnSalvar = new Button("Salvar");
        Button btnLimpar = new Button("Limpar");
        Button btnApagar = new Button("Apagar");

        painelFormulario.add(new Label("Pesquisar:"), 0, 0);
        painelFormulario.add(txtPesquisa, 1, 0);
        painelFormulario.add(btnPesquisar, 2, 0);

        painelFormulario.add(new Label("Nome:"), 0, 1);
        painelFormulario.add(txtNome, 1, 1);

        painelFormulario.add(new Label("CPF:"), 0, 2);
        painelFormulario.add(txtCpf, 1, 2);

        painelFormulario.add(new Label("Telefone:"), 0, 3);
        painelFormulario.add(txtTelefone, 1, 3);

        painelFormulario.add(new Label("Email:"), 0, 4);
        painelFormulario.add(txtEmail, 1, 4);

        painelFormulario.add(new Label("Endereço:"), 0, 5);
        painelFormulario.add(txtEndereco, 1, 5);

        painelFormulario.add(chkAtivo, 1, 6);

        painelFormulario.add(btnSalvar, 0, 7);
        painelFormulario.add(btnLimpar, 1, 7);
        painelFormulario.add(btnApagar, 2, 7);

        configurarTabela();

        painelPrincipal.setTop(painelFormulario);
        painelPrincipal.setCenter(tabela);

        btnPesquisar.setOnAction(e -> pesquisar());
        btnSalvar.setOnAction(e -> salvar());
        btnLimpar.setOnAction(e -> limpar());
        btnApagar.setOnAction(e -> apagar());

        tabela.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, antigo, novo) -> {
                    if (novo != null) {
                        clienteSelecionado = novo;
                        preencherCampos(novo);
                    }
                });

        limpar();
        pesquisar();

        return painelPrincipal;
    }

    private void configurarTabela() {
        TableColumn<Cliente, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(
                item -> new SimpleIntegerProperty(item.getValue().getId())
        );

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getNome())
        );

        TableColumn<Cliente, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getCpf())
        );

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getTelefone())
        );

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getEmail())
        );

        TableColumn<Cliente, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getEndereco())
        );

        TableColumn<Cliente, Boolean> colAtivo = new TableColumn<>("Ativo");
        colAtivo.setCellValueFactory(
                item -> new SimpleBooleanProperty(item.getValue().isAtivo())
        );

        tabela.getColumns().addAll(
                colId,
                colNome,
                colCpf,
                colTelefone,
                colEmail,
                colEndereco,
                colAtivo
        );

        tabela.setItems(control.getLista());
    }

    private void salvar() {
        try {
            clienteSelecionado.setNome(txtNome.getText());
            clienteSelecionado.setCpf(txtCpf.getText());
            clienteSelecionado.setTelefone(txtTelefone.getText());
            clienteSelecionado.setEmail(txtEmail.getText());
            clienteSelecionado.setEndereco(txtEndereco.getText());
            clienteSelecionado.setAtivo(chkAtivo.isSelected());

            control.salvar(clienteSelecionado);

            mostrarMensagem("Cliente salvo com sucesso.");
            limpar();
            pesquisar();

        } catch (Exception e) {
            mostrarMensagem(e.getMessage());
        }
    }

    private void pesquisar() {
        control.pesquisar(txtPesquisa.getText());
    }

    private void apagar() {
        if (clienteSelecionado == null || clienteSelecionado.getId() == 0) {
            mostrarMensagem("Selecione um cliente para apagar.");
            return;
        }

        control.apagar(clienteSelecionado);

        mostrarMensagem("Cliente apagado com sucesso.");
        limpar();
        pesquisar();
    }

    private void limpar() {
        clienteSelecionado = new Cliente();

        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtEndereco.clear();
        chkAtivo.setSelected(true);

        tabela.getSelectionModel().clearSelection();
    }

    private void preencherCampos(Cliente cliente) {
        txtNome.setText(cliente.getNome());
        txtCpf.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtEmail.setText(cliente.getEmail());
        txtEndereco.setText(cliente.getEndereco());
        chkAtivo.setSelected(cliente.isAtivo());
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Clientes");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}