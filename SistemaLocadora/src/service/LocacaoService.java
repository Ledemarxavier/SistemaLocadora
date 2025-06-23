package service;

import dao.LocacaoDAO;
import dao.ConexaoDAO;
import model.Cliente;
import model.Filme;
import model.FilmeFisico;
import model.FilmeStreaming;
import model.Locacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    
    public List<Locacao> listarLocacoesAtivas() throws SQLException {
        List<Locacao> locacoesAtivas = new ArrayList<>();
        String sql = "SELECT l.*, c.nome, c.cpf, c.telefone, c.email, " +
                    "f.titulo, f.ano_lancamento, f.genero, f.tipo " +
                    "FROM locacao l " +
                    "JOIN cliente c ON l.cliente_cpf = c.cpf " +
                    "JOIN filme f ON l.filme_titulo = f.titulo " +
                    "WHERE l.data_devolucao_real IS NULL " +
                    "ORDER BY l.data_locacao DESC";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
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
    
    public void darBaixaLocacao(String cpfCliente, String tituloFilme) throws SQLException {
        String sql = "UPDATE locacao SET data_devolucao_real = ?, status = 'DEVOLVIDA' " +
                    "WHERE cliente_cpf = ? AND filme_titulo = ? AND data_devolucao_real IS NULL";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(2, cpfCliente);
            stmt.setString(3, tituloFilme);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Locação não encontrada ou já devolvida");
            }
        }
    }
    
    public void verificarEstruturaBanco() throws SQLException {
        // Verificar se a coluna status existe na tabela locacao
        String checkColumnSql = "SHOW COLUMNS FROM locacao LIKE 'status'";
        boolean statusColumnExists = false;
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkColumnSql);
             ResultSet rs = stmt.executeQuery()) {
            
            statusColumnExists = rs.next();
        }
        
        if (!statusColumnExists) {
            // Adicionar coluna status se não existir
            String addColumnSql = "ALTER TABLE locacao ADD COLUMN status VARCHAR(20) DEFAULT 'ATIVA'";
            try (Connection conn = ConexaoDAO.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(addColumnSql)) {
                stmt.executeUpdate();
            }
        }
        
        // Verificar se a coluna data_devolucao_real existe
        String checkDevolucaoSql = "SHOW COLUMNS FROM locacao LIKE 'data_devolucao_real'";
        boolean devolucaoColumnExists = false;
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkDevolucaoSql);
             ResultSet rs = stmt.executeQuery()) {
            
            devolucaoColumnExists = rs.next();
        }
        
        if (!devolucaoColumnExists) {
            // Adicionar coluna data_devolucao_real se não existir
            String addDevolucaoSql = "ALTER TABLE locacao ADD COLUMN data_devolucao_real DATE NULL";
            try (Connection conn = ConexaoDAO.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(addDevolucaoSql)) {
                stmt.executeUpdate();
            }
        }
    }
    
    public void atualizarStatusLocacoes() throws SQLException {
        // Atualizar status baseado na data de devolução real
        String updateSql = "UPDATE locacao SET status = CASE " +
                          "WHEN data_devolucao_real IS NOT NULL THEN 'DEVOLVIDA' " +
                          "ELSE 'ATIVA' END";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.executeUpdate();
        }
    }
}