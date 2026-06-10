package edu.curso.dao;

import edu.curso.entidade.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario autenticar(String login, String senha) {

        String sql =
                "SELECT * FROM usuario " +
                        "WHERE login = ? AND senha = ?";

        try {

            Connection con = Conexao.conectar();

            PreparedStatement stmt =
                    con.prepareStatement(sql);

            stmt.setString(1, login);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario u = new Usuario();

                u.setId(rs.getInt("id"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setPerfil(rs.getString("perfil"));

                con.close();

                return u;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}