package br.com.alura.TabelaFipe.Service;

import java.util.List;

public interface IConverteDados {
	<T> T obterDados(String json, Class<T> classe);

	<T> List<T> obterLista(String jason, Class<T> classe);
}
