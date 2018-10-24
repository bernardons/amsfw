package com.unisys.br.amsfw.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.criteria.Order;

import com.unisys.br.amsfw.dao.exception.MuitosResultadosException;
import com.unisys.br.amsfw.domain.Entidade;

/**
 * Dao generico com os métodos da interface genérica do sistema.
 * 
 * @author delfimsm
 * 
 * @param <T>
 * @param <C>
 */
@Local
public interface GenericDAOLocal<T extends Entidade, C extends Serializable> {

	/**
	 * Remove a entidade passada como parâmetro.
	 * 
	 * @param entidade
	 */
	void remover(T entidade);

	/**
	 * Remove a entidade passada como parâmetro.
	 * 
	 * @param entidade
	 */
	void removerPorId(C id);

	/**
	 * Salva a entidade passada como parâmetro.
	 * 
	 * @param entidade
	 * @return
	 */
	T salvar(T entidade);

	/**
	 * Executa o merge da entidade passada como parâmetro.
	 * 
	 * @param entidade
	 * @return
	 */
	T merge(T entidade);

	/**
	 * Recupera o objeto por id.
	 * 
	 * @param id
	 * @return
	 */
	T recuperarPorId(C id);

	/**
	 * Recupera uma lista de objetos pelo nome da propriedade id e a lista de
	 * ids.
	 * 
	 * @param nomePropriedadeId
	 * @param ids
	 * @return
	 */
	List<T> recuperarPorIds(String nomePropriedadeId, C... ids);

	/**
	 * Recupera todos os objetos do banco com ordenação do Criteria.
	 * 
	 * @param id
	 * @return
	 * @throws MuitosResultadosException
	 */
	List<T> recuperarTodos(Order... ordenacoes) throws MuitosResultadosException;

	/**
	 * Recupera todos ordenados com ordenação como String.
	 * 
	 * @param ordenacoes
	 * @return
	 * @throws MuitosResultadosException
	 */
	List<T> recuperarTodosOrdenados(String... ordenacoes) throws MuitosResultadosException;

	/**
	 * Lista todos os usuários do sistema filtrando pelos dados preenchidos pelo
	 * usuário.
	 * 
	 * @param usuario
	 * @param indiceInicial
	 * @param quantidade
	 * @param orderBy
	 * @return
	 */
	List<T> listar(T entidadeFiltro, int indiceInicial, int quantidade, String orderBy, Object... filtrosExtras);

	/**
	 * Conta a quantidade de usuários de acordo com o usuário passado como
	 * parâmetro.
	 * 
	 * @param usuarioFiltro
	 * @return
	 */
	Long contar(T entidadeFiltro, Object... filtrosExtras);	
}
