package service;

import dao.ClienteDAO;
import model.Cliente;
import java.sql.SQLException;
import java.util.List;

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
}