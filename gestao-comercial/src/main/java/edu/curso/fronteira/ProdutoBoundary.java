package edu.curso.fronteira;

import edu.curso.controle.ProdutoControl;
import edu.curso.entidade.Produto;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ProdutoBoundary {

    private ProdutoControl control = new ProdutoControl();

    private Produto produtoSelecionado = new Produto();

    private TextField txtPesquisa = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtCategoria = new TextField();
    private TextField txtPreco = new TextField();
    private TextField txtEstoque = new TextField();
    private TextField txtFornecedor = new TextField();
    private CheckBox chkAtivo = new CheckBox("Ativo");

    private TableView<Produto> tabela = new TableView<>();

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

        painelFormulario.add(new Label("Categoria:"), 0, 2);
        painelFormulario.add(txtCategoria, 1, 2);

        painelFormulario.add(new Label("Preço:"), 0, 3);
        painelFormulario.add(txtPreco, 1, 3);

        painelFormulario.add(new Label("Estoque:"), 0, 4);
        painelFormulario.add(txtEstoque, 1, 4);

        painelFormulario.add(new Label("Fornecedor:"), 0, 5);
        painelFormulario.add(txtFornecedor, 1, 5);

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
                        produtoSelecionado = novo;
                        preencherCampos(novo);
                    }
                });

        limpar();
        pesquisar();

        return painelPrincipal;
    }

    private void configurarTabela() {
        TableColumn<Produto, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(
                item -> new SimpleIntegerProperty(item.getValue().getId())
        );

        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getNome())
        );

        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getCategoria())
        );

        TableColumn<Produto, Number> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(
                item -> new SimpleDoubleProperty(item.getValue().getPreco())
        );

        TableColumn<Produto, Number> colEstoque = new TableColumn<>("Estoque");
        colEstoque.setCellValueFactory(
                item -> new SimpleIntegerProperty(item.getValue().getEstoque())
        );

        TableColumn<Produto, String> colFornecedor = new TableColumn<>("Fornecedor");
        colFornecedor.setCellValueFactory(
                item -> new SimpleStringProperty(item.getValue().getFornecedor())
        );

        TableColumn<Produto, Boolean> colAtivo = new TableColumn<>("Ativo");
        colAtivo.setCellValueFactory(
                item -> new SimpleBooleanProperty(item.getValue().isAtivo())
        );

        tabela.getColumns().addAll(
                colId,
                colNome,
                colCategoria,
                colPreco,
                colEstoque,
                colFornecedor,
                colAtivo
        );

        tabela.setItems(control.getLista());
    }

    private void salvar() {
        try {
            produtoSelecionado.setNome(txtNome.getText());
            produtoSelecionado.setCategoria(txtCategoria.getText());
            produtoSelecionado.setPreco(Double.parseDouble(txtPreco.getText()));
            produtoSelecionado.setEstoque(Integer.parseInt(txtEstoque.getText()));
            produtoSelecionado.setFornecedor(txtFornecedor.getText());
            produtoSelecionado.setAtivo(chkAtivo.isSelected());

            control.salvar(produtoSelecionado);

            mostrarMensagem("Produto salvo com sucesso.");
            limpar();
            pesquisar();

        } catch (NumberFormatException e) {
            mostrarMensagem("Preço e estoque devem ser números válidos.");
        } catch (Exception e) {
            mostrarMensagem(e.getMessage());
        }
    }

    private void pesquisar() {
        control.pesquisar(txtPesquisa.getText());
    }

    private void apagar() {
        try {
            if (produtoSelecionado == null || produtoSelecionado.getId() == 0) {
                mostrarMensagem("Selecione um produto para apagar.");
                return;
            }

            control.apagar(produtoSelecionado);

            mostrarMensagem("Produto apagado com sucesso.");
            limpar();
            pesquisar();

        } catch (Exception e) {
            mostrarMensagem(e.getMessage());
        }
    }

    private void limpar() {
        produtoSelecionado = new Produto();

        txtNome.clear();
        txtCategoria.clear();
        txtPreco.clear();
        txtEstoque.clear();
        txtFornecedor.clear();
        chkAtivo.setSelected(true);

        tabela.getSelectionModel().clearSelection();
    }

    private void preencherCampos(Produto produto) {
        txtNome.setText(produto.getNome());
        txtCategoria.setText(produto.getCategoria());
        txtPreco.setText(String.valueOf(produto.getPreco()));
        txtEstoque.setText(String.valueOf(produto.getEstoque()));
        txtFornecedor.setText(produto.getFornecedor());
        chkAtivo.setSelected(produto.isAtivo());
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Produtos");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}