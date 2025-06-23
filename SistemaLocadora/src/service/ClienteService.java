package service;

import dao.ClienteDAO;
import dao.ConexaoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Filme;
import model.FilmeFisico;
import model.FilmeStreaming;
import model.Locacao;

public class ClienteService {
    private ClienteDAO clienteDAO;
    
    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }
    
    public void cadastrarCliente(String nome, String cpf, String telefone, String email) throws SQLException {
        Cliente cliente = new Cliente(nome, cpf, telefone, email);
        clienteDAO.cadastrar(cliente);
    }
    
    public List<Cliente> listarTodosClientes() throws SQLException {
        return clienteDAO.listarTodos();
    }
    
    public void atualizarCliente(Cliente cliente) throws SQLException {
        clienteDAO.atualizar(cliente);
    }
    
    public void removerCliente(String cpf) throws SQLException {
        clienteDAO.remover(cpf);
    }
    
    public Cliente buscarClientePorCpf(String cpf) throws SQLException {
        return clienteDAO.buscarPorCpf(cpf);
    }
    
    public List<Cliente> buscarClientesPorNome(String nome) throws SQLException {
        return clienteDAO.buscarPorNome(nome);
    }
    
    public boolean clienteTemLocacoesAtivas(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM locacao WHERE cliente_cpf = ? AND data_devolucao_real IS NULL";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        
        return false;
    }
    
    public List<Locacao> getLocacoesAtivasCliente(String cpf) throws SQLException {
        List<Locacao> locacoesAtivas = new ArrayList<>();
        String sql = "SELECT l.*, c.nome, c.cpf, c.telefone, c.email, " +
                    "f.titulo, f.ano_lancamento, f.genero, f.tipo " +
                    "FROM locacao l " +
                    "JOIN cliente c ON l.cliente_cpf = c.cpf " +
                    "JOIN filme f ON l.filme_titulo = f.titulo " +
                    "WHERE l.cliente_cpf = ? AND l.data_devolucao_real IS NULL " +
                    "ORDER BY l.data_locacao DESC";
        
        try (Connection conn = ConexaoDAO.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
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