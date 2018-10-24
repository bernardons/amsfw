package com.unisys.br.amsfw.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.exception.RecordParserException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.delimited.DelimitedFieldDescriptor;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedField;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedRecord;

/**
 * Classe responsavel por realizar as validacoes referente as posicoes do
 * arquivos e aos decorators.
 * 
 * @author regoianc
 * 
 */
public class FFPojoValidadeConstraints {

	private Object recordClazz;
	private String delimiter;

	/**
	 * Construtor para criar uma instacia do layout e recuperar o delimitador.
	 * 
	 * @throws FFPojoException
	 */
	@SuppressWarnings("rawtypes")
	public FFPojoValidadeConstraints(Class recordClazz) throws RecordParserException {
		try {
			this.recordClazz = recordClazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			delimiter = this.recordClazz.getClass().getAnnotation(DelimitedRecord.class).delimiter();
		} catch (NullPointerException e) {
			throw new RecordParserException("Não foi encontrado o delimitador do layout.");
		}
	}

	/**
	 * Realiza a validacao das posicoes e dos decorators.
	 * 
	 * @param recordClazz
	 * @param text
	 */
	public void validadeConstraints(String text) throws RecordParserException {
		List<DelimitedFieldDescriptor> delimitedFieldDescriptors = getDelimitedFieldDescriptor();
		String[] texto = text.split(delimiter);
		int tokensQtt = texto.length;
		for (int i = 0; i < delimitedFieldDescriptors.size(); i++) {
			DelimitedFieldDescriptor actualFieldDescriptor = delimitedFieldDescriptors.get(i);
			String fieldValue;
			if (actualFieldDescriptor.getPositionIndex() <= tokensQtt) {
				fieldValue = texto[actualFieldDescriptor.getPositionIndex() - 1];
			} else {
				throw new RecordParserException("As linhas do arquivo devem conter " + delimitedFieldDescriptors.size() + " posições.");
			}
			FieldDecorator<?> decorator = actualFieldDescriptor.getDecorator();
			try {
				decorator.fromString(fieldValue);
			} catch (FieldDecoratorException e) {
				throw new RecordParserException(getCauseException(decorator, fieldValue));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private String getCauseException(FieldDecorator<?> decorator, String fieldValue) {
		String erro = "Erro ao converter o valor " + fieldValue;
		try {
			ParameterizedType tipo = (ParameterizedType) decorator.getClass().getGenericInterfaces()[0];
			Class objectClass = (Class) tipo.getActualTypeArguments()[0];
			if (objectClass.equals(Integer.class) || objectClass.equals(BigInteger.class)) {
				erro += " para inteiro, verifique o tamanho";
			} else if (objectClass.equals(Date.class)) {
				erro += " para data, verifique o formato";
			} else if (objectClass.equals(BigDecimal.class)) {
				erro += " para decimal, verifique o formato";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return erro;
	}

	private List<DelimitedFieldDescriptor> getDelimitedFieldDescriptor() {
		List<DelimitedFieldDescriptor> list = new ArrayList<DelimitedFieldDescriptor>();
		for (Method metodo : recordClazz.getClass().getDeclaredMethods()) {
			DelimitedFieldDescriptor fields = new DelimitedFieldDescriptor();
			DelimitedField delimiter = metodo.getAnnotation(DelimitedField.class);
			if (delimiter != null) {
				try {
					fields.setGetter(metodo);
					fields.setPositionIndex(delimiter.positionIndex());
					fields.setDecorator(delimiter.decorator().newInstance());
					list.add(fields);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

}
