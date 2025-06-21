package service;

import dao.FilmeDAO;
import dao.LocacaoDAO;
import model.Filme;
import model.FilmeFisico;
import model.FilmeStreaming;

import java.sql.SQLException;
import java.util.List;

public class FilmeService {
    private FilmeDAO filmeDAO;
    
    public FilmeService() {
        this.filmeDAO = new FilmeDAO();
    }
    
    public void cadastrarFilmeFisico(String titulo, int ano, String genero, int numeroDisco, String tipoMidia) 
            throws SQLException {
        FilmeFisico filme = new FilmeFisico(titulo, ano, genero, numeroDisco, tipoMidia);
        filmeDAO.cadastrar(filme);
    }
    
    public void cadastrarFilmeStreaming(String titulo, int ano, String genero, String plataforma, String link) 
            throws SQLException {
        FilmeStreaming filme = new FilmeStreaming(titulo, ano, genero, plataforma, link);
        filmeDAO.cadastrar(filme);
    }
    
    public List<Filme> listarTodosFilmes() throws SQLException {
        return filmeDAO.listarTodos();
    }
    
    public void atualizarFilme(Filme filme) throws SQLException {
        filmeDAO.atualizar(filme);
    }
    
    public void removerFilme(String titulo) throws SQLException {
        filmeDAO.remover(titulo);
    }
    
    public Filme buscarFilmePorTitulo(String titulo) throws SQLException {
        return filmeDAO.buscarPorTitulo(titulo);
    }
    
    public List<Filme> listarFilmesDisponiveis() throws SQLException {
        return new LocacaoDAO().listarFilmesDisponiveis();
    }
}