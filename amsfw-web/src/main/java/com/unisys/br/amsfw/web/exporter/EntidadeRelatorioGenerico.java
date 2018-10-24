package com.unisys.br.amsfw.web.exporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Entidade com campos String para popular o relatório pdf.
 * 
 * @author EgidioRR
 * 
 */
public class EntidadeRelatorioGenerico {
	private String campo0;
	private String campo1;
	private String campo2;
	private String campo3;
	private String campo4;
	private String campo5;
	private String campo6;
	private String campo7;
	private String campo8;
	private String campo9;
	private String campo10;
	private String campo11;
	private String campo12;
	private String campo13;
	private String campo14;
	private String campo15;
	private String campo16;
	private String campo17;
	private String campo18;
	private String campo19;
	private String campo20;
	private String campo21;
	private String campo22;
	private Integer ultimoPopulado = 0;

	public void popularProximo(String valor) {
		try {
			Method metodo =
					EntidadeRelatorioGenerico.class.getMethod("setCampo" + ultimoPopulado.toString(), String.class);
			metodo.invoke(this, valor);
			ultimoPopulado++;

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String getCampo1() {
		return campo1;
	}

	public String getCampo0() {
		return campo0;
	}

	public void setCampo0(String campo0) {
		this.campo0 = campo0;
	}

	public void setCampo1(String campo1) {
		this.campo1 = campo1;
	}

	public String getCampo2() {
		return campo2;
	}

	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}

	public String getCampo3() {
		return campo3;
	}

	public void setCampo3(String campo3) {
		this.campo3 = campo3;
	}

	public String getCampo4() {
		return campo4;
	}

	public void setCampo4(String campo4) {
		this.campo4 = campo4;
	}

	public String getCampo5() {
		return campo5;
	}

	public void setCampo5(String campo5) {
		this.campo5 = campo5;
	}

	public String getCampo6() {
		return campo6;
	}

	public void setCampo6(String campo6) {
		this.campo6 = campo6;
	}

	public String getCampo7() {
		return campo7;
	}

	public void setCampo7(String campo7) {
		this.campo7 = campo7;
	}

	public String getCampo8() {
		return campo8;
	}

	public void setCampo8(String campo8) {
		this.campo8 = campo8;
	}

	public String getCampo9() {
		return campo9;
	}

	public void setCampo9(String campo9) {
		this.campo9 = campo9;
	}

	public String getCampo10() {
		return campo10;
	}

	public void setCampo10(String campo10) {
		this.campo10 = campo10;
	}

	public String getCampo11() {
		return campo11;
	}

	public void setCampo11(String campo11) {
		this.campo11 = campo11;
	}

	public String getCampo12() {
		return campo12;
	}

	public void setCampo12(String campo12) {
		this.campo12 = campo12;
	}

	public String getCampo13() {
		return campo13;
	}

	public void setCampo13(String campo13) {
		this.campo13 = campo13;
	}

	public String getCampo14() {
		return campo14;
	}

	public void setCampo14(String campo14) {
		this.campo14 = campo14;
	}

	public String getCampo15() {
		return campo15;
	}

	public void setCampo15(String campo15) {
		this.campo15 = campo15;
	}

	public String getCampo16() {
		return campo16;
	}

	public void setCampo16(String campo16) {
		this.campo16 = campo16;
	}

	public String getCampo17() {
		return campo17;
	}

	public void setCampo17(String campo17) {
		this.campo17 = campo17;
	}

	public String getCampo18() {
		return campo18;
	}

	public void setCampo18(String campo18) {
		this.campo18 = campo18;
	}

	public String getCampo19() {
		return campo19;
	}

	public void setCampo19(String campo19) {
		this.campo19 = campo19;
	}

	public String getCampo20() {
		return campo20;
	}

	public void setCampo20(String campo20) {
		this.campo20 = campo20;
	}

	public String getCampo21() {
		return campo21;
	}

	public void setCampo21(String campo21) {
		this.campo21 = campo21;
	}

	public String getCampo22() {
		return campo22;
	}

	public void setCampo22(String campo22) {
		this.campo22 = campo22;
	}

}
