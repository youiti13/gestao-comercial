package edu.curso.dao;

import edu.curso.entidade.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void salvar(Produto produto) {
        String sql = """
                INSERT INTO produto
                (nome, categoria, preco, estoque, fornecedor, ativo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.setString(5, produto.getFornecedor());
            stmt.setBoolean(6, produto.isAtivo());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Produto produto) {
        String sql = """
                UPDATE produto SET
                nome = ?,
                categoria = ?,
                preco = ?,
                estoque = ?,
                fornecedor = ?,
                ativo = ?
                WHERE id = ?
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.setString(5, produto.getFornecedor());
            stmt.setBoolean(6, produto.isAtivo());
            stmt.setInt(7, produto.getId());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apagar(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.executeUpdate();

            stmt.close();
            con.close();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Não é possível apagar este produto, pois ele possui pedido registrado."
            );
        }
    }

    public List<Produto> pesquisar(String nome) {
        List<Produto> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM produto
                WHERE nome LIKE ?
                ORDER BY nome
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, "%" + nome + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();

                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCategoria(rs.getString("categoria"));
                p.setPreco(rs.getDouble("preco"));
                p.setEstoque(rs.getInt("estoque"));
                p.setFornecedor(rs.getString("fornecedor"));
                p.setAtivo(rs.getBoolean("ativo"));

                lista.add(p);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Produto> listarTodos() {
        return pesquisar("");
    }
    public void baixarEstoque(int idProduto, int quantidade) {
        String sql = """
            UPDATE produto
            SET estoque = estoque - ?
            WHERE id = ?
            """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, quantidade);
            stmt.setInt(2, idProduto);

            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}