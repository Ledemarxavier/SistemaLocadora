package model;

public abstract class Filme {
    private String titulo;
    private int anoLancamento;
    private String genero;

    public Filme(String titulo, int anoLancamento, String genero) {
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
    }

    public abstract void exibirDetalhes();

    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + "\n" +
               "Ano: " + anoLancamento + "\n" +
               "Gênero: " + genero;
    }
}