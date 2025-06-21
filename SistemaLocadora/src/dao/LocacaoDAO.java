package dao;

import model.Locacao;
import model.Cliente;
import model.Filme;
import model.FilmeFisico;
import model.FilmeStreaming;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDAO {
    public void registrar(Locacao locacao) throws SQLException {
        String sql = "INSERT INTO locacao (cliente_id, filme_id, data_locacao, data_devolucao) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, obterIdCliente(locacao.getCliente().getCpf()));
            stmt.setInt(2, obterIdFilme(locacao.getFilme().getTitulo()));
            stmt.setDate(3, Date.valueOf(locacao.getDataLocacao()));
            stmt.setDate(4, Date.valueOf(locacao.getDataDevolucao()));
            
            stmt.executeUpdate();
        }
    }
    
    public List<Locacao> listarTodas() throws SQLException {
        List<Locacao> locacoes = new ArrayList<>();
        String sql = "SELECT l.*, c.nome, c.cpf, c.telefone, c.email, " +
                     "f.titulo, f.ano_lancamento, f.genero, f.tipo, " +
                     "ff.numero_disco, ff.tipo_midia, fs.plataforma, fs.link " +
                     "FROM locacao l " +
                     "JOIN cliente c ON l.cliente_id = c.id " +
                     "JOIN filme f ON l.filme_id = f.id " +
                     "LEFT JOIN filme_fisico ff ON f.id = ff.id " +
                     "LEFT JOIN filme_streaming fs ON f.id = fs.id";
        
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
                     "ff.numero_disco, ff.tipo_midia, fs.plataforma, fs.link " +
                     "FROM locacao l " +
                     "JOIN cliente c ON l.cliente_id = c.id " +
                     "JOIN filme f ON l.filme_id = f.id " +
                     "LEFT JOIN filme_fisico ff ON f.id = ff.id " +
                     "LEFT JOIN filme_streaming fs ON f.id = fs.id " +
                     "WHERE c.cpf = ?";
        
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
        String sql = "SELECT f.*, ff.numero_disco, ff.tipo_midia, fs.plataforma, fs.link " +
                     "FROM filme f " +
                     "LEFT JOIN filme_fisico ff ON f.id = ff.id " +
                     "LEFT JOIN filme_streaming fs ON f.id = fs.id " +
                     "WHERE f.id NOT IN (SELECT filme_id FROM locacao WHERE data_devolucao > CURDATE())";
        
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
        
        return new Locacao(cliente, filme, dataLocacao, dataDevolucao);
    }
    
    private int obterIdCliente(String cpf) throws SQLException {
        String sql = "SELECT id FROM cliente WHERE cpf = ?";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Cliente não encontrado");
    }
    
    private int obterIdFilme(String titulo) throws SQLException {
        String sql = "SELECT id FROM filme WHERE titulo = ?";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, titulo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Filme não encontrado");
    }
}
