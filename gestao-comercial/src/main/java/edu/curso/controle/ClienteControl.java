package edu.curso.controle;

import edu.curso.dao.ClienteDAO;
import edu.curso.entidade.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClienteControl {

    private ClienteDAO dao = new ClienteDAO();

    private ObservableList<Cliente> lista =
            FXCollections.observableArrayList();

    public ObservableList<Cliente> getLista() {
        return lista;
    }

    public String validar(Cliente cliente) {
        String erros = "";

        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            erros += "Nome é obrigatório.\n";
        }

        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            erros += "CPF é obrigatório.\n";
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            erros += "Telefone é obrigatório.\n";
        }

        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            erros += "Email é obrigatório.\n";
        } else if (!cliente.getEmail().contains("@")) {
            erros += "Email inválido.\n";
        }

        if (cliente.getEndereco() == null || cliente.getEndereco().isBlank()) {
            erros += "Endereço é obrigatório.\n";
        }

        return erros;
    }

    public boolean salvar(Cliente cliente) {
        String erros = validar(cliente);

        if (!erros.isBlank()) {
            throw new RuntimeException(erros);
        }

        if (cliente.getId() == 0) {
            dao.salvar(cliente);
        } else {
            dao.atualizar(cliente);
        }

        pesquisar("");
        return true;
    }

    public void apagar(Cliente cliente) {
        if (cliente != null) {
            dao.apagar(cliente.getId());
            pesquisar("");
        }
    }

    public void pesquisar(String nome) {
        lista.clear();
        lista.addAll(dao.pesquisar(nome));
    }
}