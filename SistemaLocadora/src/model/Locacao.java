package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Locacao {
    private Cliente cliente;
    private Filme filme;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;
    private LocalDate dataDevolucaoReal;
    private String status;

    public Locacao(Cliente cliente, Filme filme, LocalDate dataLocacao, LocalDate dataDevolucao) {
        this.cliente = cliente;
        this.filme = filme;
        this.dataLocacao = dataLocacao;
        this.dataDevolucao = dataDevolucao;
        this.status = "ATIVA";
        this.dataDevolucaoReal = null;
    }

    public Locacao(Cliente cliente, Filme filme, LocalDate dataLocacao, LocalDate dataDevolucao, 
                   LocalDate dataDevolucaoReal, String status) {
        this.cliente = cliente;
        this.filme = filme;
        this.dataLocacao = dataLocacao;
        this.dataDevolucao = dataDevolucao;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public LocalDate getDataLocacao() {
        return dataLocacao;
    }

    public void setDataLocacao(LocalDate dataLocacao) {
        this.dataLocacao = dataLocacao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
        if (dataDevolucaoReal != null) {
            this.status = "DEVOLVIDA";
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAtrasada() {
        if (dataDevolucaoReal != null) {
            // Se já foi devolvida, verificar se foi devolvida com atraso
            return dataDevolucaoReal.isAfter(dataDevolucao);
        } else {
            // Se não foi devolvida, verificar se está atrasada
            return LocalDate.now().isAfter(dataDevolucao);
        }
    }

    public int getDiasAtraso() {
        if (dataDevolucaoReal != null) {
            // Se foi devolvida, calcular dias de atraso na devolução
            if (dataDevolucaoReal.isAfter(dataDevolucao)) {
                return (int) ChronoUnit.DAYS.between(dataDevolucao, dataDevolucaoReal);
            }
        } else {
            // Se não foi devolvida, calcular dias de atraso atual
            if (LocalDate.now().isAfter(dataDevolucao)) {
                return (int) ChronoUnit.DAYS.between(dataDevolucao, LocalDate.now());
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("== Locação ==\n");
        sb.append("Cliente:\n").append(cliente.toString()).append("\n\n");
        sb.append("Filme:\n").append(filme.toString()).append("\n\n");
        sb.append("Data da Locação: ").append(dataLocacao).append("\n");
        sb.append("Data de Devolução: ").append(dataDevolucao).append("\n");
        sb.append("Status: ").append(status).append("\n");
        
        if (dataDevolucaoReal != null) {
            sb.append("Data de Devolução Real: ").append(dataDevolucaoReal).append("\n");
        }
        
        if (isAtrasada()) {
            sb.append("⚠️ ATRASADA - ").append(getDiasAtraso()).append(" dias de atraso\n");
        }
        
        return sb.toString();
    }
}