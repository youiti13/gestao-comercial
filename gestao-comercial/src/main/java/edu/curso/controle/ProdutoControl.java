package edu.curso.controle;

import edu.curso.dao.ProdutoDAO;
import edu.curso.entidade.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProdutoControl {
    private ProdutoDAO dao = new ProdutoDAO();

    private ObservableList<Produto> lista = FXCollections.observableArrayList();

    public ObservableList<Produto> getLista() {
        return lista;
    }

    public String validar(Produto produto){
        String erros ="";

        if(produto.getNome() == null || produto.getNome().isBlank()){
            erros += "Nome é obrigatorio. \n";
        }
        if(produto.getCategoria() == null || produto.getCategoria().isBlank()){
            erros += "Categoria é obrigatoria. \n";
        }
        if(produto.getPreco() <=0){
            erros += "O preco não pode ser menor ou igual a 0. \n";
        }
        if(produto.getEstoque() < 0){
            erros += "Estoque nao pode ser menor que 0. \n";
        }
        if(produto.getFornecedor() == null || produto.getFornecedor().isBlank()){
            erros += "Fornecedor é obrigatorio. \n";
        }
        return erros;
    }

    public boolean salvar (Produto produto){
        String erros = validar(produto);

        if(!erros.isBlank()){
            throw new RuntimeException(erros);
        }

        if(produto.getId()==0){
            dao.salvar(produto);
        }else {
            dao.atualizar(produto);
        }

        pesquisar("");
        return true;
    }

    public void apagar(Produto produto){
        if(produto != null){
            dao.apagar(produto.getId());
            pesquisar("");
        }
    }
    public void pesquisar(String nome){
        lista.clear();
        lista.addAll(dao.pesquisar(nome));
    }
}
