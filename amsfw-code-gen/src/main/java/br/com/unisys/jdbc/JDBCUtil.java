package br.com.unisys.jdbc;

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
							SystemProperties.getPropriedade(SystemProperties.CONEXAO_BANCO),
							SystemProperties.getPropriedade(SystemProperties.USUARIO_BANCO),
							SystemProperties.getPropriedade(SystemProperties.SENHA_BANCO));
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
		Statement stmt = null;
		CampoJDBC resultado = new CampoJDBC();

		String query =
				"select column_name, data_type, is_nullable, character_maximum_length, numeric_precision, "
						+ " numeric_scale, datetime_precision from INFORMATION_SCHEMA.COLUMNS where table_name = '"
						+ tabela + "' and column_name = '" + coluna + "'";
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
