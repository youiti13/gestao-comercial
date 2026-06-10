package edu.curso.dao;

import edu.curso.entidade.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void salvar(Cliente cliente) {
        String sql = """
                INSERT INTO cliente
                (nome, cpf, telefone, email, endereco, ativo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
            stmt.setBoolean(6, cliente.isAtivo());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Cliente cliente) {
        String sql = """
                UPDATE cliente SET
                nome = ?,
                cpf = ?,
                telefone = ?,
                email = ?,
                endereco = ?,
                ativo = ?
                WHERE id = ?
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
            stmt.setBoolean(6, cliente.isAtivo());
            stmt.setInt(7, cliente.getId());

            stmt.executeUpdate();

            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apagar(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

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

    public List<Cliente> pesquisar(String nome) {
        List<Cliente> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM cliente
                WHERE nome LIKE ?
                ORDER BY nome
                """;

        try {
            Connection con = Conexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, "%" + nome + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();

                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setTelefone(rs.getString("telefone"));
                c.setEmail(rs.getString("email"));
                c.setEndereco(rs.getString("endereco"));
                c.setAtivo(rs.getBoolean("ativo"));

                lista.add(c);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    public List<Cliente> listarTodos() {
        return pesquisar("");
    }
}