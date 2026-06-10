package edu.curso.dao;

import edu.curso.entidade.Cliente;
import edu.curso.entidade.Pedido;
import edu.curso.entidade.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void salvar(Pedido pedido) {
        String sql = """
                INSERT INTO pedido
                (cliente_id, produto_id, quantidade, valor_total, data_pedido, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setInt(2, pedido.getProduto().getId());
            stmt.setInt(3, pedido.getQuantidade());
            stmt.setDouble(4, pedido.getValorTotal());
            stmt.setDate(5, Date.valueOf(pedido.getDataPedido()));
            stmt.setString(6, pedido.getStatus());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Pedido pedido) {
        String sql = """
                UPDATE pedido SET
                cliente_id = ?,
                produto_id = ?,
                quantidade = ?,
                valor_total = ?,
                data_pedido = ?,
                status = ?
                WHERE id = ?
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setInt(2, pedido.getProduto().getId());
            stmt.setInt(3, pedido.getQuantidade());
            stmt.setDouble(4, pedido.getValorTotal());
            stmt.setDate(5, Date.valueOf(pedido.getDataPedido()));
            stmt.setString(6, pedido.getStatus());
            stmt.setInt(7, pedido.getId());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apagar(int id) {
        String sql = "DELETE FROM pedido WHERE id = ?";

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> pesquisar(String nomeCliente) {
        List<Pedido> lista = new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    p.quantidade,
                    p.valor_total,
                    p.data_pedido,
                    p.status,

                    c.id AS cliente_id,
                    c.nome AS cliente_nome,
                    c.cpf AS cliente_cpf,

                    pr.id AS produto_id,
                    pr.nome AS produto_nome,
                    pr.preco AS produto_preco

                FROM pedido p
                INNER JOIN cliente c ON p.cliente_id = c.id
                INNER JOIN produto pr ON p.produto_id = pr.id
                WHERE c.nome LIKE ?
                ORDER BY p.id DESC
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, "%" + nomeCliente + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cliente_id"));
                cliente.setNome(rs.getString("cliente_nome"));
                cliente.setCpf(rs.getString("cliente_cpf"));

                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                produto.setPreco(rs.getDouble("produto_preco"));

                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setCliente(cliente);
                pedido.setProduto(produto);
                pedido.setQuantidade(rs.getInt("quantidade"));
                pedido.setValorTotal(rs.getDouble("valor_total"));
                pedido.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                pedido.setStatus(rs.getString("status"));

                lista.add(pedido);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}