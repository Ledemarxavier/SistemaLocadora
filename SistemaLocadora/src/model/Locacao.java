package model;

import java.time.LocalDate;

public class Locacao {
    private Cliente cliente;
    private Filme filme;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;

    public Locacao(Cliente cliente, Filme filme, LocalDate dataLocacao, LocalDate dataDevolucao) {
        this.cliente = cliente;
        this.filme = filme;
        this.dataLocacao = dataLocacao;
        this.dataDevolucao = dataDevolucao;
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

    @Override
    public String toString() {
        return "== Locação ==\n" +
               "Cliente:\n" + cliente.toString() + "\n\n" +
               "Filme:\n" + filme.toString() + "\n\n" +
               "Data da Locação: " + dataLocacao + "\n" +
               "Data de Devolução: " + dataDevolucao;
    }
}