package br.com.alura.ScreenMatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemp(@JsonAlias("Season") Integer numeroTemp,
                        @JsonAlias("Episodes") List<DadosEp> episodios) {
}
