package model;

public class FilmeStreaming extends Filme {
    private String plataforma;
    private String link;

    public FilmeStreaming(String titulo, int anoLancamento, String genero, String plataforma, String link) {
        super(titulo, anoLancamento, genero);
        this.plataforma = plataforma;
        this.link = link;
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("Título: " + getTitulo());
        System.out.println("Ano de Lançamento: " + getAnoLancamento());
        System.out.println("Gênero: " + getGenero());
        System.out.println("Plataforma: " + plataforma);
        System.out.println("Link: " + link);
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}