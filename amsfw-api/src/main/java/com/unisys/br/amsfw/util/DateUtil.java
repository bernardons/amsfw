package com.unisys.br.amsfw.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Classe utilitária de formatação e utilitários de data.
 * 
 * @author DelfimSM
 * 
 */
public final class DateUtil {

	private static final int ULTIMO_MES_ANO = 11;
	private static final int ULTIMA_HORA_DIA = 23;
	private static final int ULTIMO_MINUTO_SEGUNDO_DATA = 59;
	private static final int TAMANHO_DATA = 10;
	private static final int HORAS = 24;
	private static final int MINUTOS = 60;
	private static final int SEGUNDOS = 60;
	private static final int MILISEGUNDOS = 1000;
	private static final String FORMATO_DDMMYYYY = "dd/MM/yyyy";
	private static List<Date> listaDate = new ArrayList<Date>(8);

	private DateUtil() {
	}

	/**
	 * Busca a hora atual do sistema no formato hora, minuto e segundo.
	 * 
	 * @return
	 */
	public static String buscaHoraAtual() {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
		return simpleDateFormat.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * Retorna a data/hora do sistema em formato dia/mes/ano hora:
	 * minuto:segundo.
	 * 
	 * @autor: SantosCO
	 * @return
	 */
	public static String buscaDataHoraAtual() {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
		return simpleDateFormat.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * Retorna a data/hora do sistema em formato dd/MM/yy.
	 * 
	 * @return
	 */
	public static String buscaDataAtual() {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", new Locale("pt", "BR"));
		return simpleDateFormat.format(cal.getTime());
	}

	/**
	 * Retorna a data/hora do sistema com hora:minuto:segundo.
	 * 
	 * @return
	 */
	public static Date getDataAtualHoraMinuto() {
		Date dataRetorno = null;

		try {
			Calendar cal = new GregorianCalendar();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));

			String data = simpleDateFormat.format(cal.getTime());
			dataRetorno = simpleDateFormat.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dataRetorno;
	}

	/**
	 * Retorna a data/hora do sistema sem hora:minuto:segundo.
	 * 
	 * @return
	 */
	public static Date getDataAtual() {
		Date dataRetorno = null;
		try {
			Calendar cal = new GregorianCalendar();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DDMMYYYY, new Locale("pt", "BR"));

			String data = simpleDateFormat.format(cal.getTime());

			dataRetorno = simpleDateFormat.parse(data);

		} catch (ParseException p) {
			p.printStackTrace();
		}

		return dataRetorno;
	}

	/**
	 * Retorna a data atual com ultima hora, ultimo minuto e ultimo segundo da
	 * hora. Exemplo: 04/11/2013 23:59:59
	 * 
	 * @return
	 */
	public static Date getDataAtualUltimaHoraMinutoSegundoDoDia() {
		return atribuirUltimaHoraMinutoSegundoNaData(new Date());
	}

	/**
	 * Atribui a data passada por parametro a ultima hora, ultimo minuto e
	 * ultimo segundo da hora.
	 * 
	 * Exemplo: Data passada = 04/11/2013 Data retornada = 04/11/2013 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date atribuirUltimaHoraMinutoSegundoNaData(Date date) {
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.setTime(date);
		dataAtual.set(Calendar.HOUR_OF_DAY, ULTIMA_HORA_DIA);
		dataAtual.set(Calendar.MINUTE, ULTIMO_MINUTO_SEGUNDO_DATA);
		dataAtual.set(Calendar.SECOND, ULTIMO_MINUTO_SEGUNDO_DATA);
		return dataAtual.getTime();

	}

	/**
	 * Atribui a data passada por parametro a primeira hora e primeiro segundo
	 * do dia.
	 * 
	 * 
	 * Exemplo: Data passada = 04/11/2013 Data retornada = 04/11/2013 00:00:01
	 * 
	 * @param date
	 * @return
	 */
	public static Date atribuirPrimeiraHoraMinutoSegundoNaData(Date date) {
		GregorianCalendar dataAtual = new GregorianCalendar();
		dataAtual.setTime(date);
		dataAtual.set(Calendar.HOUR_OF_DAY, 0);
		dataAtual.set(Calendar.MINUTE, 0);
		dataAtual.set(Calendar.SECOND, 1);
		return dataAtual.getTime();

	}

	/**
	 * Retorna uma data sendo o dia passado por parametro e o mes e ano atual do
	 * sistema.
	 * 
	 * @param diaParametro
	 * @return
	 */
	public static Date getMesAnoAtualComDiaPassadoPorParametro(int diaParametro) {
		Calendar dataAtualCalendar = GregorianCalendar.getInstance();

		Calendar dataParametrizada =
				new GregorianCalendar(dataAtualCalendar.get(Calendar.YEAR), dataAtualCalendar.get(Calendar.MONTH), diaParametro);

		return atribuirUltimaHoraMinutoSegundoNaData(dataParametrizada.getTime());
	}

	/**
	 * Retorna a data/hora do sistema em formato dd/MM/yyyy.
	 * 
	 * @return
	 */
	public static String buscaDataAtualAAAA() {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DDMMYYYY, new Locale("pt", "BR"));
		return simpleDateFormat.format(cal.getTime());
	}

	/**
	 * Formata data no formato dd/MM/yyyy.
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String formataData(Date dataFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DDMMYYYY, new Locale("pt", "BR"));
		return simpleDateFormat.format(dataFormat.getTime());
	}

	/**
	 * Formata data no formato MM/yyyy.
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String formataDataMesAno(Date dataFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy", new Locale("pt", "BR"));
		return simpleDateFormat.format(dataFormat.getTime());
	}

	/**
	 * Formata data no formato dd/MM/yyyy HH:mm:ss.
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String formataDataHoraMinuto(Date dataFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
		return simpleDateFormat.format(dataFormat.getTime());
	}

	/**
	 * Formata data no formato dd/MM/yyyy HH:mm:ss.
	 * 
	 * @param dataFormat
	 * @return
	 * @throws ParseException
	 */
	public static Date formataDataHoraMinutoSemSeparador(String data) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmm", new Locale("pt", "BR"));
		return simpleDateFormat.parse(data);
	}

	/**
	 * Formata data no formato dd/MM/yyyy HH:mm:ss.
	 * 
	 * @param dataFormat
	 * @return
	 * @throws ParseException
	 */
	public static Date formataDataHoraMinutoSemSeparadorParser(String data) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm", new Locale("pt", "BR"));
		return simpleDateFormat.parse(data);
	}

	/**
	 * Retorna a String data no formato ddMMyyyyHHmm.
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String getDataHoraMinutoSemSeparadorParser(Date data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm", new Locale("pt", "BR"));
		return simpleDateFormat.format(data);
	}

	/**
	 * Retorna a String data no formato ddMMyyyyHHmm.
	 * 
	 * @param dataFormat
	 * @return
	 */
	public static String getDataHoraMinutoSemSeparador(Date data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmm", new Locale("pt", "BR"));
		return simpleDateFormat.format(data);
	}

	/**
	 * Gera map com ano e mes atual.
	 * 
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static Map buscaAnoMesAtual() {

		Calendar cal = new GregorianCalendar();
		int mes = (cal.get(Calendar.MONTH)) + 1;
		int ano = cal.get(Calendar.YEAR);
		Map mapAnoMes = new LinkedHashMap();
		mapAnoMes.put("ano", Integer.valueOf(ano));
		mapAnoMes.put("mes", Integer.valueOf(mes));
		return mapAnoMes;
	}

	/**
	 * Gera map com data atual menos a quantidade de meses passadas por
	 * parametro.
	 * 
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static Map buscaAnoMesAnteriorAoParametro(int totalMesesAnteriores) {

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -totalMesesAnteriores);
		int mes = (cal.get(Calendar.MONTH)) + 1;
		int ano = cal.get(Calendar.YEAR);
		Map mapAnoMes = new LinkedHashMap();
		mapAnoMes.put("ano", Integer.valueOf(ano));
		mapAnoMes.put("mes", Integer.valueOf(mes));
		return mapAnoMes;
	}

	/**
	 * Gera String com mm/yyyy da data passada por parametro.
	 * 
	 * @return
	 */
	public static String gerarMesAnoData(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int mes = (cal.get(Calendar.MONTH)) + 1;
		int ano = cal.get(Calendar.YEAR);
		return String.format("%02d", mes) + "/" + ano;
	}

	/**
	 * Gera map com ano e mes anterior ao atual.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map buscaAnoMesAnterior() {
		return buscaMesAnoAtualMenosMesesPassadoPorParametro(1);
	}

	/**
	 * Gera map com ano e mes, sendo o mes: mês atual menos a quantidade des
	 * meses passado por parametro.
	 * 
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static Map buscaMesAnoAtualMenosMesesPassadoPorParametro(int subtrairMeses) {

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -subtrairMeses);
		int mes = (cal.get(Calendar.MONTH)) + 1;
		int ano = cal.get(Calendar.YEAR);
		Map mapAnoMes = new LinkedHashMap();
		mapAnoMes.put("ano", Integer.valueOf(ano));
		mapAnoMes.put("mes", Integer.valueOf(mes));
		return mapAnoMes;
	}

	/**
	 * Gera map com ano e mes anterior ao parametro.
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static Map buscaAnoMesAnterior(Integer ano, Integer mes) {
		Calendar data = new GregorianCalendar();
		data.set(Calendar.YEAR, ano.intValue());
		data.set(Calendar.MONTH, mes.intValue() - 1);
		data.add(Calendar.MONTH, -1);
		int mes1 = (data.get(Calendar.MONTH) + 1);
		int ano1 = data.get(Calendar.YEAR);
		Map mapAnoMes = new LinkedHashMap();
		mapAnoMes.put("ano", Integer.valueOf(ano1));
		mapAnoMes.put("mes", Integer.valueOf(mes1));
		return mapAnoMes;
	}

	/**
	 * Verifica se o parametro informado está preenchido.
	 * 
	 * @param parametro
	 *            O campo a ser verificado.
	 * @return <i>true</i> Se o parametro estiver preenchido, <i>false</i> caso
	 *         contrário.
	 */
	public static boolean estaPreenchido(String parametro) {
		boolean retorno = true;

		if ((parametro == null) || (parametro.trim().length() < 1)) {
			retorno = false;
		}

		return retorno;
	}

	/**
	 * Adiciona quantidade de meses a data passada.
	 * 
	 * @param data
	 * @param numMes
	 * @return data com quantidade de meses adicionada.
	 */
	public static Date adicionaMes(Date data, int numMes) {

		Calendar dataLimite = Calendar.getInstance();
		dataLimite.setTime(data);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DDMMYYYY, new Locale("pt", "BR"));

		dataLimite.add(Calendar.MONTH, numMes);

		String data1 = simpleDateFormat.format(dataLimite.getTime());

		Date dataRetorno = null;
		try {
			dataRetorno = simpleDateFormat.parse(data1);

		} catch (ParseException p) {
			p.printStackTrace();
		}

		return dataRetorno;

	}

	/**
	 * Adiciona quantidade de dias a data passada.
	 * 
	 * @param data
	 * @param numDias
	 * @return data com quantidade de dias adicionada.
	 */
	public static Date adicionaDias(Date data, int numDias) {

		Calendar dataLimite = Calendar.getInstance();
		dataLimite.setTime(data);
		dataLimite.add(Calendar.DAY_OF_MONTH, numDias);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DDMMYYYY, new Locale("pt", "BR"));

		String data1 = simpleDateFormat.format(dataLimite.getTime());

		Date dataRetorno = null;
		try {
			dataRetorno = simpleDateFormat.parse(data1);

		} catch (ParseException p) {
			p.printStackTrace();
		}

		return dataRetorno;

	}

	/**
	 * Primeiro milissegundo de Data.
	 * 
	 * @param data
	 *            Calendar
	 * @return
	 */
	public static Date primeiroMilissegundoDeData(Calendar data) {
		Calendar work = (Calendar) data.clone();
		work.set(Calendar.HOUR_OF_DAY, Integer.parseInt("0"));
		work.set(Calendar.MINUTE, Integer.parseInt("0"));
		work.set(Calendar.SECOND, Integer.parseInt("0"));
		work.set(Calendar.MILLISECOND, Integer.parseInt("0"));
		return work.getTime();
	}

	/**
	 * Primeiro milissegundo de Hoje.
	 * 
	 * @return
	 */
	public static Date primeiroMilissegundoDeHoje() {
		return primeiroMilissegundoDeData(GregorianCalendar.getInstance());
	}

	/**
	 * Primeiro milissegundo de Data.
	 * 
	 * @param data
	 *            Date
	 * @return
	 */
	public static Date primeiroMilissegundoDeData(Date data) {
		Calendar work = Calendar.getInstance();
		work.setTime(data);
		return primeiroMilissegundoDeData(work);
	}

	/**
	 * Ultimo milissegundo de Data.
	 * 
	 * @param data
	 *            Calendar
	 * @return
	 */
	public static Date ultimoMilissegundoDeData(Calendar data) {
		Calendar work = (Calendar) data.clone();
		work.set(Calendar.HOUR_OF_DAY, Integer.parseInt("23"));
		work.set(Calendar.MINUTE, Integer.parseInt("59"));
		work.set(Calendar.SECOND, Integer.parseInt("59"));
		work.set(Calendar.MILLISECOND, Integer.parseInt("999"));
		return work.getTime();
	}

	/**
	 * Ultimo milissegundo de Hoje.
	 * 
	 * @return
	 */
	public static Date ultimoMilissegundoDeHoje() {
		return ultimoMilissegundoDeData(GregorianCalendar.getInstance());
	}

	/**
	 * Ultimo milissegundo de Data.
	 * 
	 * @param data
	 *            Date
	 * @return
	 */
	public static Date ultimoMilissegundoDeData(Date data) {
		Calendar work = Calendar.getInstance();
		work.setTime(data);
		return ultimoMilissegundoDeData(work);
	}

	/**
	 * Retorna o dia da semana por extenso.
	 * 
	 * @param i
	 * @param tipo
	 * @return
	 */
	public static String diaSemana(int i) {
		String[] diasem = {"Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado" };

		return (diasem[i - 1]); // extenso
	}

	/**
	 * Retorna o mês por extenso.
	 * 
	 * @param mes
	 * @param tipo
	 * @return
	 */
	public static String getMesExtenso(int mes) {
		if (mes > ULTIMO_MES_ANO || mes < 0) {
			return null;
		}

		String[] meses =
				{
						"Janeiro",
						"Fevereiro",
						"Março",
						"Abril",
						"Maio",
						"Junho",
						"Julho",
						"Agosto",
						"Setembro",
						"Outubro",
						"Novembro",
						"Dezembro" };

		return (meses[mes]);
	}

	/**
	 * Retorna o mês por extenso abreviado.
	 * 
	 * @param mes
	 * @return
	 */
	public static String getMesExtensoAbreviado(int mes) {
		if (mes > ULTIMO_MES_ANO || mes < 0) {
			return null;
		}

		String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };

		return (meses[mes]);
	}

	/**
	 * Retorna a data atual por extenso.
	 * 
	 * @return
	 */
	public static String dataAtualPorExtenso() {

		Calendar dataAtual = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy, HH:mm", new Locale("pt", "BR"));
		String dataFormatada = simpleDateFormat.format(dataAtual.getTime());

		return (diaSemana(dataAtual.get(Calendar.DAY_OF_WEEK)) + " " + dataFormatada);
	}

	/**
	 * Retorna a data atual por extenso.
	 * 
	 * @return
	 */
	public static String dataPorExtenso(Date data) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy, HH:mm", new Locale("pt", "BR"));
		String dataFormatada = simpleDateFormat.format(data.getTime());

		return (diaSemana(calendar.get(Calendar.DAY_OF_WEEK)) + " " + dataFormatada);
	}

	/**
	 * Retorna a data com ultimo dia do mês.
	 * 
	 * @return
	 */
	public static Date dataUltimoDiaMes(int mes, int ano) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, mes + 1);
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);

		Date ultimoDiaDoMes = calendar.getTime();

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("Last Day of Month: " + sdf.format(ultimoDiaDoMes));

		return ultimoDiaDoMes;

	}

	/**
	 * Retorna a data com ultimo dia do mês.
	 * 
	 * @return
	 */
	public static Date dataUltimoDiaMes(String dataMesAno) {

		String[] data = dataMesAno.split("/");

		if (data.length != 2) {
			return null;
		}

		return dataUltimoDiaMes(Integer.parseInt(data[0]) - 1, Integer.parseInt(data[1]));

	}

	/**
	 * Retorna a data com primeiro dia do mês.
	 * 
	 * @return
	 */

	public static Date dataUltimoDiaMes(Date data) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.MONTH, +1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();

	}

	/**
	 * Retorna o utlimo dia do mes passado por parametro.
	 * 
	 * @return
	 */

	public static int getUltimoDiaMes(Date data) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.MONTH, +1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);

		return calendar.get(Calendar.DAY_OF_MONTH);

	}

	/**
	 * Retorna a data com primeiro dia do mes.
	 * 
	 * @param data
	 * @return
	 */
	public static Date dataPrimeiroDiaMes(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();

	}

	/**
	 * Retorna a data com primeiro dia do mes.
	 * 
	 * @param dataMesAno
	 * @return
	 */
	public static Date dataPrimeiroDiaMes(String dataMesAno) {

		String[] data = dataMesAno.split("/");

		if (data.length != 2) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Integer.parseInt(data[0]) - 1);
		calendar.set(Calendar.YEAR, Integer.parseInt(data[1]));

		return calendar.getTime();
	}

	/**
	 * Converte uma data do tipo String no formato brasileiro para Date <br>
	 * <b>Exemplo:</b><br>
	 * <i>converteStringParaDate("23/09/2013"); <br>
	 * Resultado será a String 23/09/2013 convertida para java.util.Date</i>.
	 * 
	 * @param data
	 *            a ser convertida.
	 * @return String convertida em Date caso essa String esteja nula, branca ou
	 *         seu tamanho diferente de 10 o retorno é <i>null</i>
	 */
	public static Date converteStringParaDate(String data) {
		if (data != null && data != "" && data.length() == TAMANHO_DATA) {
			try {
				return new SimpleDateFormat("dd/MM/yyyyy").parse(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Retorna a diferença de dias entre a primeira e a segunda data.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int getDiferencaDias(Date dataInicio, Date dataFim) {
		long diferencaMilisegundos = dataFim.getTime() - dataInicio.getTime();
		return (int) (diferencaMilisegundos / MILISEGUNDOS / SEGUNDOS / MINUTOS / HORAS);

	}

	/**
	 * Retorna o ano da data passado por parametro.
	 * 
	 * @param data
	 * @return
	 */
	public static int getAnoDaData(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * Retorna o dia da data passada por parametro.
	 * 
	 * @param data
	 * @return
	 */
	public static int getDiaDaData(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Verifica se o dia passado como parametro é o ultimo dia do mês, caso ele
	 * não seja retorna o ultimo dia.
	 * 
	 * @author RegoIanC
	 * @param dia
	 * @return Ultimo dia do mes
	 */
	public static int verificarUltimoDiaMesCorrente(int dia) {

		int ultimoDia = getUltimoDiaMes(new Date());

		if (dia > ultimoDia) {
			return ultimoDia;
		}
		return dia;

	}

	/**
	 * Retorna o mês posterior ao mês passado por parametro. Exemplo: Se você
	 * passar o mês de dezembro, então será retornado mês de janeiro.
	 * 
	 * @param mes
	 * @return
	 */
	public static int getMesPosteriorMesPassadoPorParametro(int mes) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.add(Calendar.MONTH, 1);
		return calendar.get(Calendar.MONTH) + 1;

	}

	/**
	 * Retorna o mês anterior ao mês passado por parametro. Exemplo: Se você
	 * passar o mês de janeiro, então será retornado mês de dezembro.
	 * 
	 * @param mes
	 * @return
	 */
	public static int getMesAnteriorMesPassadoPorParametro(int mes) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.add(Calendar.MONTH, -1);
		return calendar.get(Calendar.MONTH) + 1;

	}

	/**
	 * Subtrai do mes passado por parametro a quantidade de meses passados no
	 * segundo parametro.
	 * 
	 * Exemplo: Se você passar no primeiro parametro o mês de abril e no segundo
	 * parametro o inteiro 2, será retornado o mês de fevereiro.
	 * 
	 * @param mes
	 * @param quantidadeMesesSeraSubtraida
	 * @return
	 */
	public static int subtrairMeses(int mes, int quantidadeMesesSeraSubtraida) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.add(Calendar.MONTH, -quantidadeMesesSeraSubtraida);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * Compara se a data 1 e a data 2 são de meses e/ou ano diferente.
	 * 
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static boolean isDatasSaoMesesOuAnoDiferentes(Date data1, Date data2) {

		if (data1 == null) {
			return false;
		}

		Calendar data1Calendar = GregorianCalendar.getInstance();
		data1Calendar.setTime(data1);

		Calendar data2Calendar = GregorianCalendar.getInstance();
		data2Calendar.setTime(data2);

		int mesData1 = data1Calendar.get(Calendar.MONTH);
		int anoData1 = data1Calendar.get(Calendar.YEAR);

		int mesData2 = data2Calendar.get(Calendar.MONTH);
		int anoData2 = data2Calendar.get(Calendar.YEAR);

		if (mesData1 != mesData2 || anoData1 != anoData2) {
			return true;
		}
		return false;

	}

	/**
	 * Obtem a data por extenso junto a hora.
	 * 
	 * @return
	 */
	public static String getDataHoraAtual() {
		Calendar calendar = Calendar.getInstance();
		return new SimpleDateFormat("dd 'de " + DateUtil.getMesExtenso(calendar.get(Calendar.MONTH)) + " de' yyyy HH:mm:ss")
				.format(calendar.getTime());

	}

	/**
	 * Valida se o dia passado é um dia util. Sao considerados feriados
	 * NACIONAIS e Final de Semana como criterio para saber se e ou nao dia
	 * util.
	 * 
	 * @author RegoIanC & OliveiMH
	 * @param data
	 * @return
	 */
	public static boolean isDiaUtil(Date data) {
		criarFeriadosNacionais();
		for (Date item : listaDate) {
			if (isDiaMesIgual(data, item) || isFDS(data)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Lista que contem todos os feriados nacionais.
	 * 
	 * @author RegoIanC & OliveiMH
	 * @return
	 */
	private static void criarFeriadosNacionais() {

		try {
			SimpleDateFormat format = new SimpleDateFormat("MM-dd");
			listaDate.add(format.parse("02-07"));
			listaDate.add(format.parse("01-01"));
			listaDate.add(format.parse("04-21"));
			listaDate.add(format.parse("05-01"));
			listaDate.add(format.parse("09-07"));
			listaDate.add(format.parse("10-12"));
			listaDate.add(format.parse("11-02"));
			listaDate.add(format.parse("11-15"));
			listaDate.add(format.parse("12-25"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Valida se a data passada e final de semana.
	 * 
	 * @author RegoIanC & OliveiMH
	 * @param data
	 * @return
	 */
	private static boolean isFDS(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? true : false;
	}

	/**
	 * Compara o mes e o ano.
	 * 
	 * @param atual
	 * @param feriado
	 * @return
	 */
	private static boolean isDiaMesIgual(Date atual, Date feriado) {
		Calendar calAtual = Calendar.getInstance();
		Calendar calFeriado = Calendar.getInstance();
		calAtual.setTime(atual);
		calFeriado.setTime(feriado);
		calFeriado.set(Calendar.YEAR, calAtual.get(Calendar.YEAR));
		atual = DateUtil.atribuirUltimaHoraMinutoSegundoNaData(calAtual.getTime());
		feriado = DateUtil.atribuirUltimaHoraMinutoSegundoNaData(calFeriado.getTime());
		if (atual.compareTo(feriado) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * De acorodo com a data passada recupera o proximo dia util caso a data nao
	 * seja.
	 * 
	 * @author RegoIanC & OliveiMH
	 * @param date
	 * @return
	 */
	public static Date recuperarProximoDiaUtil(Date date) {
		while (!DateUtil.isDiaUtil(date)) {
			date = DateUtil.adicionaDias(date, 1);
		}
		return date;
	}
}