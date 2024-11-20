package br.com.alura.ScreenMatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {

    private final Integer temporada;
    private final String titulo;
    private final Integer numeroEp;
    private Double avaliacao;
    private LocalDate dataDeLancamento;

    public Episodio(Integer numTemp, DadosEp dadosEp){
        this.temporada = numTemp;
        this.titulo = dadosEp.titulo();
        this.numeroEp = dadosEp.numeroEp();

        try{
        this.avaliacao = Double.valueOf(dadosEp.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try {
        this.dataDeLancamento = LocalDate.parse(dadosEp.dataDeLancamento());
        }catch(DateTimeParseException ex){
            this.dataDeLancamento = null;
        }
    }

    public Integer getTemporada() {
        return temporada;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEp=" + numeroEp +
                ", avaliacao=" + avaliacao +
                ", dataDeLancamento=" + dataDeLancamento;
    }
}
