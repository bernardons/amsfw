package br.com.unisys.modelo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import br.com.unisys.service.ClasseUtil;

/**
 * Classe que possui os dados para a geração do código.
 * 
 * @author DelfimSM
 * 
 */
@SuppressWarnings("rawtypes")
public class ModeloBase {

	private Class classe;

	private String pacoteDominio;

	private String pacoteProjeto;
	
	private String entidade;

	private TipoGeracaoEnum tipoGeracao;

	private String autor;

	private String tipoChave;

	private String paginaPesquisa;

	private String paginaCadastro;

	private String funcionalidade;

	private String grupoBreadcrumb;

	private List<Field> camposPesquisa;

	private List<Field> camposListaPesquisa;

	private List<Field> camposCadastro;

	private Map<String, Field> mapaMetodos = null;

	private List<String> listaMetodos = null;

	public String getPaginaPesquisa() {
		return paginaPesquisa;
	}

	public String getPaginaPesquisaCompleta() {
		return "/pages/" + getPacoteDominio() + "/" + paginaPesquisa;
	}

	public void setPaginaPesquisa(String paginaPesquisa) {
		this.paginaPesquisa = paginaPesquisa;
	}

	public String getPacoteDominio() {
		return pacoteDominio;
	}

	public void setPacoteDominio(String pacoteDominio) {
		this.pacoteDominio = pacoteDominio;
	}
	
	public String getPacoteProjeto() {
		return pacoteProjeto;
	}
	
	public void setPacoteProjeto(String pacoteProjeto) {
		this.pacoteProjeto = pacoteProjeto;
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	/**
	 * Recupera o tipo de geração da tela.
	 * 
	 * @return
	 */
	public TipoGeracaoEnum getTipoGeracao() {
		if (tipoGeracao == null) {
			tipoGeracao = TipoGeracaoEnum.CRUD_COMPLETO;
		}
		return tipoGeracao;
	}

	public void setTipoGeracao(TipoGeracaoEnum tipoGeracao) {
		this.tipoGeracao = tipoGeracao;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTipoChave() {
		return tipoChave;
	}

	public void setTipoChave(String tipoChave) {
		this.tipoChave = tipoChave;
	}

	public String getPaginaCadastro() {
		return paginaCadastro;
	}

	public void setPaginaCadastro(String paginaCadastro) {
		this.paginaCadastro = paginaCadastro;
	}

	public String getPaginaCadastroCompleta() {
		return "/pages/" + getPacoteDominio() + "/" + paginaCadastro;
	}

	public String getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(String funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public String getGrupoBreadcrumb() {
		return grupoBreadcrumb;
	}

	public void setGrupoBreadcrumb(String grupoBreadcrumb) {
		this.grupoBreadcrumb = grupoBreadcrumb;
	}

	/**
	 * Recupera a entidade com a primeira letra minuscula.
	 * 
	 * @return
	 */
	public String getEntidadePrimeiraLetraMinuscula() {
		if (entidade != null && entidade.length() > 0) {
			return entidade.substring(0, 1).toLowerCase() + entidade.substring(1);
		}
		return null;
	}

	public List<Field> getCamposPesquisa() {
		return camposPesquisa;
	}

	public void setCamposPesquisa(List<Field> camposPesquisa) {
		this.camposPesquisa = camposPesquisa;
	}

	public List<Field> getCamposListaPesquisa() {
		return camposListaPesquisa;
	}

	public void setCamposListaPesquisa(List<Field> camposListaPesquisa) {
		this.camposListaPesquisa = camposListaPesquisa;
	}

	public List<Field> getCamposCadastro() {
		return camposCadastro;
	}

	public void setCamposCadastro(List<Field> camposCadastro) {
		this.camposCadastro = camposCadastro;
	}

	public Class getClasse() {
		return classe;
	}

	public void setClasse(Class classe) {
		this.classe = classe;
	}

	public String getNomeCompletoClasse() {
		return classe.getName();
	}

	public Map<String, Field> getMapaMetodos() {
		return mapaMetodos;
	}

	public void setMapaMetodos(Map<String, Field> mapaMetodos) {
		this.mapaMetodos = mapaMetodos;
	}

	public List<String> getListaMetodos() {
		return listaMetodos;
	}

	public void setListaMetodos(List<String> listaMetodos) {
		this.listaMetodos = listaMetodos;
	}

	public List<String> getListaCamposCadastro() {
		return ClasseUtil.getListaCamposCadastro(classe, camposCadastro);
	}

	public List<String> getListaCamposPesquisa() {
		return ClasseUtil.getListaCamposTabelaPesquisa(classe, camposListaPesquisa);
	}

	public List<String> getListaCamposPesquisaDAO() {
		return ClasseUtil.getListaStringPesquisaDAO(classe, camposPesquisa);
	}

	/**
	 * Recupera o nome completo da entidade.
	 * 
	 * @return
	 */
	public String getNomeCompletoEntidade() {
		String[] nomeDividido = ClasseUtil.getStringDivididaPorLetraMaiuscula(classe.getSimpleName());
		StringBuilder builder = new StringBuilder();

		for (String atual : nomeDividido) {
			if (!atual.equals("")) {
				builder.append(atual.substring(0, 1).toUpperCase() + atual.substring(1, atual.length()) + " ");
			}
		}

		return builder.toString().substring(0, builder.toString().length() - 1);
	}

	public List<String> getListaCamposFiltro() {
		return ClasseUtil.getListaCamposFiltro(classe, camposPesquisa);
	}

	/**
	 * Recupera a String default do pacote de domínio.
	 * 
	 * @return
	 */
	public String getStringDefaultPacoteDominio() {

		String valor = classe.getName();
		valor = valor.substring(0, valor.lastIndexOf("."));
		valor = valor.substring(valor.lastIndexOf(".") + 1, valor.length());

		return valor;
	}

	public String getStringDefaultPaginaPesquisa() {
		return "pesquisar" + classe.getSimpleName() + ".xhtml";
	}

	public String getStringDefaultPaginaCadastro() {
		return "cadastrar" + classe.getSimpleName() + ".xhtml";
	}

	/**
	 * Recupera a String default de Funcionalidade.
	 * 
	 * @return
	 */
	public String getStringDefaultFuncionalidade() {

		String[] nomeDividido = ClasseUtil.getStringDivididaPorLetraMaiuscula(classe.getSimpleName());

		StringBuilder builder = new StringBuilder();
		builder.append("manter_");
		for (String atual : nomeDividido) {
			if (!atual.equals("")) {
				String valor = atual.substring(0, 1).toLowerCase() + atual.substring(1);
				builder.append(valor).append("_");
			}
		}

		return builder.toString().substring(0, builder.toString().length() - 1);
	}

	public String getStringDefaultTipoChave() {
		return ClasseUtil.getTipoChave(classe.getName());
	}

	/**
	 * Método de teste.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String valor = ModeloBase.class.getName();
		System.out.println(valor);
		valor = valor.substring(0, valor.lastIndexOf("."));
		System.out.println(valor);
		valor = valor.substring(valor.lastIndexOf(".") + 1, valor.length());
		System.out.println(valor);

		String[] pacote = valor.split(".");
		System.out.println(pacote.length);
	}

}
