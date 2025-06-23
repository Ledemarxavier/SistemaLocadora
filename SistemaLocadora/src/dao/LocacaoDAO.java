package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Filme;
import model.FilmeFisico;
import model.FilmeStreaming;
import model.Locacao;

public class LocacaoDAO {
    public void registrar(Locacao locacao) throws SQLException {
        String sql = "INSERT INTO locacao (cliente_cpf, filme_titulo, data_locacao, data_devolucao, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, locacao.getCliente().getCpf());
            stmt.setString(2, locacao.getFilme().getTitulo());
            stmt.setDate(3, Date.valueOf(locacao.getDataLocacao()));
            stmt.setDate(4, Date.valueOf(locacao.getDataDevolucao()));
            stmt.setString(5, locacao.getStatus());
            
            stmt.executeUpdate();
        }
    }
    
    public List<Locacao> listarTodas() throws SQLException {
        List<Locacao> locacoes = new ArrayList<>();
        String sql = "SELECT l.*, c.nome, c.cpf, c.telefone, c.email, " +
                     "f.titulo, f.ano_lancamento, f.genero, f.tipo, " +
                     "f.numero_disco, f.tipo_midia, f.plataforma, f.link " +
                     "FROM locacao l " +
                     "JOIN cliente c ON l.cliente_cpf = c.cpf " +
                     "JOIN filme f ON l.filme_titulo = f.titulo " +
                     "ORDER BY l.data_locacao DESC";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                locacoes.add(criarLocacaoFromResultSet(rs));
            }
        }
        return locacoes;
    }
    
    public List<Locacao> buscarPorCliente(String cpf) throws SQLException {
        List<Locacao> locacoes = new ArrayList<>();
        String sql = "SELECT l.*, c.nome, c.cpf, c.telefone, c.email, " +
                     "f.titulo, f.ano_lancamento, f.genero, f.tipo, " +
                     "f.numero_disco, f.tipo_midia, f.plataforma, f.link " +
                     "FROM locacao l " +
                     "JOIN cliente c ON l.cliente_cpf = c.cpf " +
                     "JOIN filme f ON l.filme_titulo = f.titulo " +
                     "WHERE c.cpf = ? " +
                     "ORDER BY l.data_locacao DESC";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    locacoes.add(criarLocacaoFromResultSet(rs));
                }
            }
        }
        return locacoes;
    }
    
    public List<Filme> listarFilmesDisponiveis() throws SQLException {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT f.* FROM filme f " +
                     "WHERE f.titulo NOT IN (SELECT filme_titulo FROM locacao WHERE data_devolucao_real IS NULL)";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String titulo = rs.getString("titulo");
                int ano = rs.getInt("ano_lancamento");
                String genero = rs.getString("genero");
                
                if ("FISICO".equals(tipo)) {
                    int numeroDisco = rs.getInt("numero_disco");
                    String tipoMidia = rs.getString("tipo_midia");
                    filmes.add(new FilmeFisico(titulo, ano, genero, numeroDisco, tipoMidia));
                } else {
                    String plataforma = rs.getString("plataforma");
                    String link = rs.getString("link");
                    filmes.add(new FilmeStreaming(titulo, ano, genero, plataforma, link));
                }
            }
        }
        return filmes;
    }
    
    private Locacao criarLocacaoFromResultSet(ResultSet rs) throws SQLException {
        // Criar cliente
        Cliente cliente = new Cliente(
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("telefone"),
            rs.getString("email")
        );
        
        // Criar filme
        Filme filme;
        String tipo = rs.getString("tipo");
        String titulo = rs.getString("titulo");
        int ano = rs.getInt("ano_lancamento");
        String genero = rs.getString("genero");
        
        if ("FISICO".equals(tipo)) {
            int numeroDisco = rs.getInt("numero_disco");
            String tipoMidia = rs.getString("tipo_midia");
            filme = new FilmeFisico(titulo, ano, genero, numeroDisco, tipoMidia);
        } else {
            String plataforma = rs.getString("plataforma");
            String link = rs.getString("link");
            filme = new FilmeStreaming(titulo, ano, genero, plataforma, link);
        }
        
        // Criar locação
        LocalDate dataLocacao = rs.getDate("data_locacao").toLocalDate();
        LocalDate dataDevolucao = rs.getDate("data_devolucao").toLocalDate();
        LocalDate dataDevolucaoReal = rs.getDate("data_devolucao_real") != null ? 
            rs.getDate("data_devolucao_real").toLocalDate() : null;
        String status = rs.getString("status");
        
        return new Locacao(cliente, filme, dataLocacao, dataDevolucao, dataDevolucaoReal, status);
    }
}