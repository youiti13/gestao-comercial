package edu.curso.controle;

import edu.curso.dao.PedidoDAO;
import edu.curso.dao.ProdutoDAO;
import edu.curso.entidade.Pedido;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class PedidoControl {

    private PedidoDAO dao = new PedidoDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    private ObservableList<Pedido> lista =
            FXCollections.observableArrayList();

    public ObservableList<Pedido> getLista() {
        return lista;
    }

    public String validar(Pedido pedido) {
        String erros = "";

        if (pedido.getCliente() == null) {
            erros += "Cliente é obrigatório.\n";
        }

        if (pedido.getProduto() == null) {
            erros += "Produto é obrigatório.\n";
        }

        if (pedido.getQuantidade() <= 0) {
            erros += "Quantidade deve ser maior que zero.\n";
        }

        // Valida estoque apenas ao criar pedido novo
        if (pedido.getId() == 0 &&
                pedido.getProduto() != null &&
                pedido.getQuantidade() > pedido.getProduto().getEstoque()) {
            erros += "Estoque insuficiente.\n";
        }

        if (pedido.getDataPedido() == null) {
            erros += "Data do pedido é obrigatória.\n";
        }

        if (pedido.getStatus() == null || pedido.getStatus().isBlank()) {
            erros += "Status é obrigatório.\n";
        }

        return erros;
    }

    public boolean salvar(Pedido pedido) {
        String erros = validar(pedido);

        if (!erros.isBlank()) {
            throw new RuntimeException(erros);
        }

        double valorTotal =
                pedido.getQuantidade() *
                        pedido.getProduto().getPreco();

        pedido.setValorTotal(valorTotal);

        if (pedido.getDataPedido() == null) {
            pedido.setDataPedido(LocalDate.now());
        }

        if (pedido.getId() == 0) {
            dao.salvar(pedido);

            produtoDAO.baixarEstoque(
                    pedido.getProduto().getId(),
                    pedido.getQuantidade()
            );
        } else {
            dao.atualizar(pedido);
        }

        pesquisar("");
        return true;
    }

    public void apagar(Pedido pedido) {
        if (pedido != null) {
            dao.apagar(pedido.getId());
            pesquisar("");
        }
    }

    public void pesquisar(String nomeCliente) {
        lista.clear();
        lista.addAll(dao.pesquisar(nomeCliente));
    }
}