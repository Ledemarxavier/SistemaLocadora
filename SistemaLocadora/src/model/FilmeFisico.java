package model;

public class FilmeFisico extends Filme {
    private int numeroDisco;
    private String tipoMidia;

    public FilmeFisico(String titulo, int anoLancamento, String genero, int numeroDisco, String tipoMidia) {
        super(titulo, anoLancamento, genero);
        this.numeroDisco = numeroDisco;
        this.tipoMidia = tipoMidia;
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("Título: " + getTitulo());
        System.out.println("Ano de Lançamento: " + getAnoLancamento());
        System.out.println("Gênero: " + getGenero());
        System.out.println("Número do Disco: " + numeroDisco);
        System.out.println("Tipo de Mídia: " + tipoMidia);
    }

    
    public int getNumeroDisco() {
        return numeroDisco;
    }

    public void setNumeroDisco(int numeroDisco) {
        this.numeroDisco = numeroDisco;
    }

    public String getTipoMidia() {
        return tipoMidia;
    }

    public void setTipoMidia(String tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
               "Tipo: Físico\n" +
               "Nº do Disco: " + numeroDisco + "\n" +
               "Mídia: " + tipoMidia;
    }
}