package service;

import dao.LocacaoDAO;
import model.Cliente;
import model.Filme;
import model.Locacao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LocacaoService {
    private LocacaoDAO locacaoDAO;
    
    public LocacaoService() {
        this.locacaoDAO = new LocacaoDAO();
    }
    
    public void registrarLocacao(Cliente cliente, Filme filme, LocalDate dataLocacao, LocalDate dataDevolucao) 
            throws SQLException {
        Locacao locacao = new Locacao(cliente, filme, dataLocacao, dataDevolucao);
        locacaoDAO.registrar(locacao);
    }
    
    public List<Locacao> listarTodasLocacoes() throws SQLException {
        return locacaoDAO.listarTodas();
    }
    
    public List<Locacao> buscarLocacoesPorCliente(String cpf) throws SQLException {
        return locacaoDAO.buscarPorCliente(cpf);
    }
    
    public List<Filme> listarFilmesDisponiveis() throws SQLException {
        return locacaoDAO.listarFilmesDisponiveis();
    }
}