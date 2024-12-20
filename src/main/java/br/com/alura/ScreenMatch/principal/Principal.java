package br.com.alura.ScreenMatch.principal;

import br.com.alura.ScreenMatch.model.DadosEp;
import br.com.alura.ScreenMatch.model.DadosSerie;
import br.com.alura.ScreenMatch.model.DadosTemp;
import br.com.alura.ScreenMatch.model.Episodio;
import br.com.alura.ScreenMatch.service.ConsumoApi;
import br.com.alura.ScreenMatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner scan = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=dc476d66";

    public void exibeMenu(){

        System.out.println("Digite o nome da série para a busca");
        var nomeSerie = scan.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosDaSerie = conversor.obterDados(json, DadosSerie.class);

        System.out.println(dadosDaSerie);
        System.out.println();

        List<DadosTemp> temporadas = new ArrayList<>();

		for(int i = 1; i <= dadosDaSerie.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+ "&season=" + i + API_KEY);
			DadosTemp dadosDaTemp = conversor.obterDados(json, DadosTemp.class);
			temporadas.add(dadosDaTemp);
		}

		temporadas.forEach(System.out::println);
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        System.out.println();

//        List<DadosEp> dadosEps = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//
//        System.out.println("Top 5 Episódios:");
//        dadosEps.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEp::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);
//
//        System.out.println("\nEpisódios:");

        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemp(), d)))
                        .collect(Collectors.toList());

        episodio.forEach(System.out::println);
//
//        System.out.println("Digite um trecho do título desejado");
//        var trechoTitulo = scan.nextLine().toUpperCase();
//
//        Optional<Episodio> epBuscado = episodio.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo))
//                .findFirst();
//
//        if(epBuscado.isPresent()){
//            System.out.println("Ep encontrado");
//            System.out.println("Temporada: " + epBuscado.get().getTemporada());
//        }else{
//            System.out.println("Ep não encontrado");
//        }
//
//        System.out.println("A partir de qual ano você deseja ver os episódios?");
//        var ano = scan.nextInt();
//        scan.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodio.stream()
//                .filter(e -> e.getDataDeLancamento() != null && e.getDataDeLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataDeLancamento().format(formatador)
//                ));

        Map<Integer, Double> avaliacaoPorTemp = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacaoPorTemp);

        DoubleSummaryStatistics est = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Média total: " + est.getAverage());
        System.out.println("Melhor Ep: " + est.getMax());
        System.out.println("Pior Ep: " + est.getMin());

    }
}
