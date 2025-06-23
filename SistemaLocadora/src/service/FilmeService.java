package service;

import dao.FilmeDAO;
import dao.LocacaoDAO;
import dao.ConexaoDAO;
import model.Filme;
import model.FilmeFisico;
import model.FilmeStreaming;
import model.Locacao;
import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    
    public boolean filmeTemLocacoesAtivas(String titulo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM locacao WHERE filme_titulo = ? AND data_devolucao_real IS NULL";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        
        return false;
    }
    
    public List<Locacao> getLocacoesAtivasFilme(String titulo) throws SQLException {
        List<Locacao> locacoesAtivas = new ArrayList<>();
        String sql = "SELECT l.*, c.nome, c.cpf, c.telefone, c.email, " +
                    "f.titulo, f.ano_lancamento, f.genero, f.tipo " +
                    "FROM locacao l " +
                    "JOIN cliente c ON l.cliente_cpf = c.cpf " +
                    "JOIN filme f ON l.filme_titulo = f.titulo " +
                    "WHERE l.filme_titulo = ? AND l.data_devolucao_real IS NULL " +
                    "ORDER BY l.data_locacao DESC";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email")
                );
                
                Filme filme = null;
                String tipo = rs.getString("tipo");
                if ("FISICO".equals(tipo)) {
                    filme = new FilmeFisico(
                        rs.getString("titulo"),
                        rs.getInt("ano_lancamento"),
                        rs.getString("genero"),
                        rs.getInt("numero_disco"),
                        rs.getString("tipo_midia")
                    );
                } else if ("STREAMING".equals(tipo)) {
                    filme = new FilmeStreaming(
                        rs.getString("titulo"),
                        rs.getInt("ano_lancamento"),
                        rs.getString("genero"),
                        rs.getString("plataforma"),
                        rs.getString("link")
                    );
                }
                
                Locacao locacao = new Locacao(
                    cliente,
                    filme,
                    rs.getDate("data_locacao").toLocalDate(),
                    rs.getDate("data_devolucao").toLocalDate(),
                    rs.getDate("data_devolucao_real") != null ? 
                        rs.getDate("data_devolucao_real").toLocalDate() : null,
                    rs.getString("status")
                );
                
                locacoesAtivas.add(locacao);
            }
        }
        
        return locacoesAtivas;
    }
}