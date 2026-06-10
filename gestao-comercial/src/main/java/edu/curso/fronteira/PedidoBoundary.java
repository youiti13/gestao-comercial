package edu.curso.fronteira;

import edu.curso.controle.PedidoControl;
import edu.curso.dao.ClienteDAO;
import edu.curso.dao.ProdutoDAO;
import edu.curso.entidade.Cliente;
import edu.curso.entidade.Pedido;
import edu.curso.entidade.Produto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class PedidoBoundary {

    private PedidoControl control = new PedidoControl();

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    private Pedido pedidoSelecionado = new Pedido();

    private TextField txtPesquisa = new TextField();
    private ComboBox<Cliente> cmbCliente = new ComboBox<>();
    private ComboBox<Produto> cmbProduto = new ComboBox<>();
    private TextField txtQuantidade = new TextField();
    private DatePicker dtPedido = new DatePicker();
    private ComboBox<String> cmbStatus = new ComboBox<>();

    private TableView<Pedido> tabela = new TableView<>();

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

        carregarCombos();

        painelFormulario.add(new Label("Pesquisar Cliente:"), 0, 0);
        painelFormulario.add(txtPesquisa, 1, 0);
        painelFormulario.add(btnPesquisar, 2, 0);

        painelFormulario.add(new Label("Cliente:"), 0, 1);
        painelFormulario.add(cmbCliente, 1, 1);

        painelFormulario.add(new Label("Produto:"), 0, 2);
        painelFormulario.add(cmbProduto, 1, 2);

        painelFormulario.add(new Label("Quantidade:"), 0, 3);
        painelFormulario.add(txtQuantidade, 1, 3);

        painelFormulario.add(new Label("Data:"), 0, 4);
        painelFormulario.add(dtPedido, 1, 4);

        painelFormulario.add(new Label("Status:"), 0, 5);
        painelFormulario.add(cmbStatus, 1, 5);

        painelFormulario.add(btnSalvar, 0, 6);
        painelFormulario.add(btnLimpar, 1, 6);
        painelFormulario.add(btnApagar, 2, 6);

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
                        pedidoSelecionado = novo;
                        preencherCampos(novo);
                    }
                });

        limpar();
        pesquisar();

        return painelPrincipal;
    }

    private void carregarCombos() {
        cmbCliente.getItems().setAll(clienteDAO.listarTodos());
        cmbProduto.getItems().setAll(produtoDAO.listarTodos());

        cmbStatus.getItems().setAll(
                "ABERTO",
                "PAGO",
                "CANCELADO",
                "ENTREGUE"
        );
    }

    private void configurarTabela() {
        TableColumn<Pedido, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(
                item -> new SimpleIntegerProperty(item.getValue().getId())
        );

        TableColumn<Pedido, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getCliente().getNome())
        );

        TableColumn<Pedido, String> colProduto = new TableColumn<>("Produto");
        colProduto.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getProduto().getNome())
        );

        TableColumn<Pedido, Number> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(
                item -> new SimpleIntegerProperty(item.getValue().getQuantidade())
        );

        TableColumn<Pedido, Number> colValorTotal = new TableColumn<>("Valor Total");
        colValorTotal.setCellValueFactory(
                item -> new SimpleDoubleProperty(item.getValue().getValorTotal())
        );

        TableColumn<Pedido, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(
                item -> new SimpleStringProperty(String.valueOf(item.getValue().getDataPedido()))
        );

        TableColumn<Pedido, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getStatus())
        );

        tabela.getColumns().addAll(
                colId,
                colCliente,
                colProduto,
                colQuantidade,
                colValorTotal,
                colData,
                colStatus
        );

        tabela.setItems(control.getLista());
    }

    private void salvar() {
        try {
            pedidoSelecionado.setCliente(cmbCliente.getValue());
            pedidoSelecionado.setProduto(cmbProduto.getValue());
            pedidoSelecionado.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
            pedidoSelecionado.setDataPedido(dtPedido.getValue());
            pedidoSelecionado.setStatus(cmbStatus.getValue());

            control.salvar(pedidoSelecionado);

            mostrarMensagem("Pedido salvo com sucesso.");
            limpar();
            pesquisar();

        } catch (NumberFormatException e) {
            mostrarMensagem("Quantidade deve ser um número inteiro.");
        } catch (Exception e) {
            mostrarMensagem(e.getMessage());
        }
    }

    private void pesquisar() {
        control.pesquisar(txtPesquisa.getText());
    }

    private void apagar() {
        if (pedidoSelecionado == null || pedidoSelecionado.getId() == 0) {
            mostrarMensagem("Selecione um pedido para apagar.");
            return;
        }

        control.apagar(pedidoSelecionado);

        mostrarMensagem("Pedido apagado com sucesso.");
        limpar();
        pesquisar();
    }

    private void limpar() {
        pedidoSelecionado = new Pedido();

        cmbCliente.setValue(null);
        cmbProduto.setValue(null);
        txtQuantidade.clear();
        dtPedido.setValue(LocalDate.now());
        cmbStatus.setValue("ABERTO");

        tabela.getSelectionModel().clearSelection();
    }

    private void preencherCampos(Pedido pedido) {
        cmbCliente.setValue(pedido.getCliente());
        cmbProduto.setValue(pedido.getProduto());
        txtQuantidade.setText(String.valueOf(pedido.getQuantidade()));
        dtPedido.setValue(pedido.getDataPedido());
        cmbStatus.setValue(pedido.getStatus());
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pedidos");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}