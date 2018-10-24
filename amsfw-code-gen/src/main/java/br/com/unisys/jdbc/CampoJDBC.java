package br.com.unisys.jdbc;

/**
 * Objeto que representa o campo no banco de dados.
 * 
 * @author DelfimSM
 *
 */
public class CampoJDBC {

	private String nomeCampo;

	private String tipo;

	private boolean permiteNulo;

	private int tamanhoString;

	private int precisaoNumerica;

	private int escalaNumerica;

	private int precisaoData;

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isPermiteNulo() {
		return permiteNulo;
	}

	public void setPermiteNulo(boolean permiteNulo) {
		this.permiteNulo = permiteNulo;
	}

	public int getTamanhoString() {
		return tamanhoString;
	}

	public void setTamanhoString(int tamanhoString) {
		this.tamanhoString = tamanhoString;
	}

	public int getPrecisaoNumerica() {
		return precisaoNumerica;
	}

	public void setPrecisaoNumerica(int precisaoNumerica) {
		this.precisaoNumerica = precisaoNumerica;
	}

	public int getEscalaNumerica() {
		return escalaNumerica;
	}

	public void setEscalaNumerica(int escalaNumerica) {
		this.escalaNumerica = escalaNumerica;
	}

	public int getPrecisaoData() {
		return precisaoData;
	}

	public void setPrecisaoData(int precisaoData) {
		this.precisaoData = precisaoData;
	}

}
