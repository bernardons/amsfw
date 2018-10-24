package com.unisys.br.amsfw.test.testbuilder;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitária de construção de entidades de teste.
 * 
 * @author DelfimSM
 * 
 */
public final class TestBuilderUtil {

	private static final Logger LOG = LoggerFactory.getLogger(TestBuilderUtil.class);

	private TestBuilderUtil() {
	}

	/**
	 * método que recupera todos os builders.
	 * 
	 * @param classes
	 * @return
	 */
	public static Collection<Class<? extends TestBuilder>> getBuilder(Class<? extends TestBuilder>... classes) {
		return TestBuilderUtil.getCollection(classes);
	}

	/**
	 * Retorna uma coleção tipada com o tipo T passado um parâmetro.
	 * 
	 * @param params
	 * @return
	 */
	public static <T> Collection<T> getCollection(T... params) {
		Collection<T> colecao = new ArrayList<T>();
		for (T param : params) {
			colecao.add(param);
		}

		return colecao;
	}

	/**
	 * Retorna toda a lista de contrutores de um objetos de teste assim como sua
	 * lista de dependências.
	 * 
	 * @param nomesArquivosDados
	 * @param builders
	 * @return
	 */
	public static Collection<String> getAllBuilders(Collection<String> nomesArquivosDados,
		Collection<Class<? extends TestBuilder>> builders, Collection<Class<? extends TestBuilder>> carregados) {

		if (nomesArquivosDados == null) {
			nomesArquivosDados = new ArrayList<String>();
		}

		if (builders != null) {
			try {
				for (Class<? extends TestBuilder> classeBuilder : builders) {

					//Evita Loop Infinito
					if (!carregados.contains(classeBuilder)) {
						TestBuilder objeto = classeBuilder.newInstance();
						// Itera em todas as dependências
						if (objeto.getDependencias() != null) {
							//Adiciona a lista de builders já carregados.
							carregados.add(classeBuilder);
							getAllBuilders(nomesArquivosDados, objeto.getDependencias(), carregados);
						}

						// Adiciona arquivos XLS do objeto atual
						Collection<String> nomesArquivosDadosIteracao = objeto.getNomesArquivosDados();
						if (nomesArquivosDadosIteracao != null) {
							for (String valor : nomesArquivosDadosIteracao) {
								String nomeClasse = objeto.getClass().getName();
								String nomePacoteAtual = "/" + nomeClasse.substring(0, nomeClasse.lastIndexOf(".") + 1);
								nomePacoteAtual = nomePacoteAtual.replace(".", "/");
								valor = nomePacoteAtual + valor;

								if (!nomesArquivosDados.contains(valor)) {
									nomesArquivosDados.add(valor);
								}
							}
						}
					}
				}
			} catch (InstantiationException e) {
				LOG.error("Problema ao instanciar o objeto.", e);
			} catch (IllegalAccessException e) {
				LOG.error("Acesso ilegal ao objeto.", e);
			}
		}

		return nomesArquivosDados;
	}
}
