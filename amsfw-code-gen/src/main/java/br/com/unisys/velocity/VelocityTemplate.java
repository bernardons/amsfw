package br.com.unisys.velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import br.com.unisys.modelo.ModeloBase;
import br.com.unisys.util.SystemProperties;

/**
 * Classe que retorna os valores do velocity.
 * 
 * @author DelfimSM
 * 
 */
public class VelocityTemplate {

	private VelocityEngine velocityEngine;

	/**
	 * Construtor do velocity Template.
	 * 
	 */
	public VelocityTemplate() {
		try {
			// Load the velocity properties from the class path
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream("velocity.properties"));

			// Create and initialize the template engine
			velocityEngine = new VelocityEngine(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método de execução do template.
	 * 
	 * @param modeloBase
	 * @param template
	 * @return
	 */
	public String execute(ModeloBase modeloBase, String template) {

		try {
			// Build a context to hold the model
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("modelo", modeloBase);

			// Execute the template
			StringWriter writer = new StringWriter();
			velocityEngine.mergeTemplate(template, "utf-8", velocityContext, writer);

			// Return the result
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String getTextoVelocity(String nomeTemplate, ModeloBase modeloBase) {
		VelocityTemplate template = new VelocityTemplate();
		return template.execute(modeloBase, nomeTemplate);
	}

	/**
	 * Retorna o conteúdo do Persist Controller.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getConteudoPersistController(ModeloBase modeloBase) {
		return getTextoVelocity("templates/" + SystemProperties.getPropriedade(SystemProperties.NOME_PROJETO)
				+ "/web/controller/XXXPersistController.vm", modeloBase);
	}

	/**
	 * Retorna o arquivo do persist controller.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getArquivoPersistController(ModeloBase modeloBase) {
		return SystemProperties.getDiretorioController() + "/" + modeloBase.getPacoteDominio() + "/"
				+ modeloBase.getEntidade() + "PersistController.java";
	}

	/**
	 * Retorna o conteúdo do Search Controller.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getConteudoSearchController(ModeloBase modeloBase) {
		return getTextoVelocity("templates/" + SystemProperties.getPropriedade(SystemProperties.NOME_PROJETO)
				+ "/web/controller/XXXSearchController.vm", modeloBase);
	}

	/**
	 * Retorna o arquivo do Search Controller.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getArquivoSearchController(ModeloBase modeloBase) {
		return SystemProperties.getDiretorioController() + "/" + modeloBase.getPacoteDominio() + "/"
				+ modeloBase.getEntidade() + "SearchController.java";
	}

	/**
	 * Retorna o conteúdo da página de pesquisa.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getConteudoPaginaPesquisa(ModeloBase modeloBase) {
		return getTextoVelocity("templates/" + SystemProperties.getPropriedade(SystemProperties.NOME_PROJETO)
				+ "/web/pages/pesquisarXXX.vm", modeloBase);
	}

	/**
	 * Retorna o arquivo da página de pesquisa.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getArquivoPaginaPesquisa(ModeloBase modeloBase) {
		return SystemProperties.getDiretorioPaginasWeb() + "/" + modeloBase.getPacoteDominio() + "/pesquisar"
				+ modeloBase.getEntidade() + ".xhtml";
	}

	/**
	 * Retorna o conteúdo da página de cadastro.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getConteudoPaginaCadastro(ModeloBase modeloBase) {
		return getTextoVelocity("templates/" + SystemProperties.getPropriedade(SystemProperties.NOME_PROJETO)
				+ "/web/pages/cadastrarXXX.vm", modeloBase);
	}

	/**
	 * Retorna o arquivo da página de cadastro.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getArquivoPaginaCadastro(ModeloBase modeloBase) {
		return SystemProperties.getDiretorioPaginasWeb() + "/" + modeloBase.getPacoteDominio() + "/cadastrar"
				+ modeloBase.getEntidade() + ".xhtml";
	}

	/**
	 * Retorna o conteúdo do DAOLocal.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getConteudoDAOLocal(ModeloBase modeloBase) {
		return getTextoVelocity("templates/" + SystemProperties.getPropriedade(SystemProperties.NOME_PROJETO)
				+ "/api/dao/XXXDAOLocal.vm", modeloBase);
	}

	/**
	 * Retorna o arquivo do DAO Local.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getArquivoDAOLocal(ModeloBase modeloBase) {
		return SystemProperties.getDiretorioInterfacesDao() + "/" + modeloBase.getPacoteDominio() + "/"
				+ modeloBase.getEntidade() + "DAOLocal.java";
	}

	/**
	 * Retorna o conteúdo do DAO.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getConteudoDAO(ModeloBase modeloBase) {
		return getTextoVelocity("templates/" + SystemProperties.getPropriedade(SystemProperties.NOME_PROJETO)
				+ "/ejb/dao/XXXDAO.vm", modeloBase);
	}

	/**
	 * Retorna o arquivo do DAO.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static String getArquivoDAO(ModeloBase modeloBase) {
		return SystemProperties.getDiretorioImplementacaoDao() + "/" + modeloBase.getPacoteDominio() + "/"
				+ modeloBase.getEntidade() + "DAO.java";
	}

	private static boolean gravarArquivoSeNaoExistir(String nomeArquivo, String conteudoArquivo) {

		FileOutputStream fop = null;
		File file;

		try {
			file = new File(nomeArquivo);

			if (!file.exists()) {
				criaDiretorio(nomeArquivo);
				file.createNewFile();
			} else {
				return false;
			}

			fop = new FileOutputStream(file);

			// get the content in bytes
			byte[] contentInBytes = conteudoArquivo.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	private static boolean criaDiretorio(String nomeArquivo) {
		String diretorio = nomeArquivo.substring(0, nomeArquivo.lastIndexOf("/"));

		final File dir = new File(diretorio);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return true;
	}

	/**
	 * Grava a página de pesquisa.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarPaginaPesquisa(ModeloBase modeloBase) {
		return gravarArquivoSeNaoExistir(getArquivoPaginaPesquisa(modeloBase), getConteudoPaginaPesquisa(modeloBase));
	}

	/**
	 * Grava a página de cadastro.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarPaginaCadastro(ModeloBase modeloBase) {
		return gravarArquivoSeNaoExistir(getArquivoPaginaCadastro(modeloBase), getConteudoPaginaCadastro(modeloBase));
	}

	/**
	 * Grava o Search Controller.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarSearchController(ModeloBase modeloBase) {
		return gravarArquivoSeNaoExistir(
				getArquivoSearchController(modeloBase),
				getConteudoSearchController(modeloBase));
	}

	/**
	 * Grava o persist controller.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarPersistController(ModeloBase modeloBase) {
		return gravarArquivoSeNaoExistir(
				getArquivoPersistController(modeloBase),
				getConteudoPersistController(modeloBase));
	}

	/**
	 * Grava o DAO Local.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarDAOLocal(ModeloBase modeloBase) {
		return gravarArquivoSeNaoExistir(getArquivoDAOLocal(modeloBase), getConteudoDAOLocal(modeloBase));
	}

	/**
	 * Graca o DAO.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarDAO(ModeloBase modeloBase) {
		return gravarArquivoSeNaoExistir(getArquivoDAO(modeloBase), getConteudoDAO(modeloBase));
	}

	/**
	 * Grava o crudo completo.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarCrudCompleto(ModeloBase modeloBase) {
		gravarPaginaPesquisa(modeloBase);
		gravarPaginaCadastro(modeloBase);
		gravarDAOLocal(modeloBase);
		gravarDAO(modeloBase);
		gravarPersistController(modeloBase);
		gravarSearchController(modeloBase);

		return true;
	}

	/**
	 * Grava somente a pesquisa.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarSomentePesquisa(ModeloBase modeloBase) {
		gravarPaginaPesquisa(modeloBase);
		gravarDAOLocal(modeloBase);
		gravarDAO(modeloBase);
		gravarSearchController(modeloBase);

		return true;
	}

	/**
	 * Grava somente o cadastro.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarSomenteCadastro(ModeloBase modeloBase) {
		gravarPaginaCadastro(modeloBase);
		gravarDAOLocal(modeloBase);
		gravarDAO(modeloBase);
		gravarPersistController(modeloBase);

		return true;
	}

	/**
	 * Grava o DAO e DAO Local.
	 * 
	 * @param modeloBase
	 * @return
	 */
	public static boolean gravarDAOeDAOLocal(ModeloBase modeloBase) {
		gravarDAOLocal(modeloBase);
		gravarDAO(modeloBase);

		return true;
	}

}
