package br.com.alura.TabelaFipe.Principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.TabelaFipe.Model.Dados;
import br.com.alura.TabelaFipe.Model.Modelos;
import br.com.alura.TabelaFipe.Model.Veiculo;
import br.com.alura.TabelaFipe.Service.ConsumoApi;
import br.com.alura.TabelaFipe.Service.ConverteDados;

public class Principal {
	private Scanner leitura = new Scanner(System.in);
	private ConsumoApi consumo = new ConsumoApi();
	private ConverteDados conversor = new ConverteDados();
	
	private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

	public void exibeMenu() {
		var menu = """
				*** OPÇÕES ***
				- Carro
				- Moto
				- Caminhão
				
				Digite uma das opções para Consultar """;

		System.out.println( menu);
		var opcao = leitura.nextLine();
		
		String endereco = null;
		if (opcao.toLowerCase().contains("carr")) {
			endereco = URL_BASE + "carros/marcas";
		}else if (opcao.toLowerCase().contains("mot")) {
			endereco = URL_BASE + "motos/marcas";
		}else if (opcao.toLowerCase().contains("camin")) {
			endereco = URL_BASE + "caminhoes/marcas";
		}else {
			System.out.println("Informe uma opção valida!");
			return;
		}

		String json = consumo.obterDados(endereco);
		System.out.println(json);
		
		var marcas = conversor.obterLista(json, Dados.class);
		marcas.stream()
				.sorted(Comparator.comparing(Dados::codigo))
				.forEach(System.out::println);
		
		System.out.println("Informe o código da marca para consultar");
		var codigoMarca = leitura.nextLine();
		
		endereco = endereco + "/" + codigoMarca + "/modelos";
		json = consumo.obterDados(endereco);
		var modeloLista = conversor.obterDados(json, Modelos.class);
		
		System.out.println("/nModelos dessa marca:");
		modeloLista.modelos().stream()
								.sorted(Comparator.comparing(Dados::codigo))
									.forEach(System.out::println);
		
		System.out.println("\nDigite um trecho do nome do carro a ser buscado");
		var nomeVeiculo = leitura.nextLine();
		
		List<Dados> modelosFiltrados = modeloLista.modelos().stream()
				.filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
				.collect(Collectors.toList());
		
		System.out.println("\nModelos filtrados");
		modelosFiltrados.forEach(System.out::println);
		
		System.out.println("Digite o codigo do modelo para buscar os valores de avaliacao");
		var codigoModelo = leitura.nextLine();
		
		endereco = endereco + "/" + codigoModelo + "/anos";
		json = consumo.obterDados(endereco);
		List<Dados> anos = conversor.obterLista(json, Dados.class);
		List<Veiculo> veiculos = new ArrayList<>();
		
		for (int i = 0; i < anos.size(); i++) {
			var enderecoAnos = endereco + "/" + anos.get(i).codigo();
			json = consumo.obterDados(enderecoAnos);
			Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
			veiculos.add(veiculo);
		}
		
		System.out.println("\nTODOS OS VEICULOS FILTRADOS COM AVALIACOES POR ANO:");
		veiculos.forEach(System.out::println);
	}

}















