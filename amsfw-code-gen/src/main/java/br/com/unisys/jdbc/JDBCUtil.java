package br.com.unisys.jdbc;

import static br.com.unisys.jdbc.BancoDeDados.ORACLE;
import static br.com.unisys.jdbc.BancoDeDados.POSTGRES;
import static br.com.unisys.util.SystemProperties.BANCO;
import static br.com.unisys.util.SystemProperties.CONEXAO_BANCO;
import static br.com.unisys.util.SystemProperties.SENHA_BANCO;
import static br.com.unisys.util.SystemProperties.USUARIO_BANCO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.unisys.util.SystemProperties;

/**
 * Classe do JDBC.
 * 
 * @author DelfimSM
 * 
 */
public class JDBCUtil {
	
	private JDBCUtil() {
	}


	private static Connection getConnection() {
		Connection connection = null;

		try {
			connection =
					DriverManager.getConnection(
							SystemProperties.getPropriedade(CONEXAO_BANCO),
							SystemProperties.getPropriedade(USUARIO_BANCO),
							SystemProperties.getPropriedade(SENHA_BANCO));
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}

		return connection;

	}
	
	/**
	 * Recupera os dados da coluna.
	 * 
	 * @param tabela
	 * @param coluna
	 * @return
	 */
	public static CampoJDBC getDadosColunas(String tabela, String coluna) {
		final String banco = SystemProperties.getPropriedade(BANCO).toUpperCase();
		if (ORACLE.name().equals(banco)) {
			return getDadosColunasOracle(tabela, coluna);
		} else if (POSTGRES.name().equals(banco)) {
			return getDadosColunasPostgreSQL(tabela, coluna);
		} else {
			throw new RuntimeException("Propriedade 'banco' inválida (" + banco + "). Valores válidos: ['oracle', 'postgree']!");
		}
	}

	/**
	 * Recupera os dados da coluna no Banco de Dados ORACLE.
	 * 
	 * @param tabela
	 * @param coluna
	 * @return
	 */
	private static CampoJDBC getDadosColunasOracle(String tabela, String coluna) {
		Statement stmt = null;
		CampoJDBC resultado = new CampoJDBC();

		String query =
				"select COLUMN_NAME, DATA_TYPE, NULLABLE, CHAR_COL_DECL_LENGTH, DATA_PRECISION, "
						+ " DATA_SCALE from ALL_TAB_COLS where TABLE_NAME = '"
						+ tabela + "' and COLUMN_NAME = '" + coluna + "'";
		try {
			stmt = getConnection().createStatement();

			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				resultado.setNomeCampo(rs.getString("COLUMN_NAME")); // COLUMN_NAME
				resultado.setTipo(rs.getString("DATA_TYPE")); // DATA_TYPE
				resultado.setPermiteNulo(!"N".equals(rs.getString("NULLABLE").toUpperCase())); // NULLABLE
				resultado.setTamanhoString(rs.getInt("CHAR_COL_DECL_LENGTH")); // CHAR_COL_DECL_LENGTH
				resultado.setPrecisaoNumerica(rs.getInt("DATA_PRECISION")); // DATA_PRECISION
				resultado.setEscalaNumerica(rs.getInt("DATA_SCALE")); // DATA_SCALE
				resultado.setPrecisaoData(6); // datetime_precision
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return resultado;

	}
	

	/**
	 * Recupera os dados da coluna no Banco de Dados PostgreSQL.
	 * 
	 * @param tabela
	 * @param coluna
	 * @return
	 */
	public static CampoJDBC getDadosColunasPostgreSQL(String tabela, String coluna) {
		Statement stmt = null;
		CampoJDBC resultado = new CampoJDBC();

		String query =
				"select column_name, data_type, is_nullable, character_maximum_length, numeric_precision, "
						+ " numeric_scale, datetime_precision from INFORMATION_SCHEMA.COLUMNS where lower(table_name) = lower('"
						+ tabela + "') and lower(column_name) = lower('" + coluna + "')";
		try {
			stmt = getConnection().createStatement();

			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				resultado.setNomeCampo(rs.getString("column_name"));
				resultado.setTipo(rs.getString("data_type"));
				resultado.setPermiteNulo(rs.getBoolean("is_nullable"));
				resultado.setTamanhoString(rs.getInt("character_maximum_length"));
				resultado.setPrecisaoNumerica(rs.getInt("numeric_precision"));
				resultado.setEscalaNumerica(rs.getInt("numeric_scale"));
				resultado.setPrecisaoData(rs.getInt("datetime_precision"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return resultado;

	}	

}
