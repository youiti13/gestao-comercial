package edu.curso.controle;

import edu.curso.dao.UsuarioDAO;
import edu.curso.entidade.Usuario;
import edu.curso.seguranca.Sessao;

public class LoginControl {

    private UsuarioDAO dao =
            new UsuarioDAO();

    public boolean autenticar(
            String login,
            String senha) {

        Usuario usuario =
                dao.autenticar(login, senha);

        if (usuario != null) {

            Sessao.setUsuarioLogado(usuario);

            return true;
        }

        return false;
    }
}