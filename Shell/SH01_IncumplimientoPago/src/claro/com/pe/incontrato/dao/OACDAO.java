package claro.com.pe.incontrato.dao;

import claro.com.pe.incontrato.bean.PagoPuntual;
import claro.com.pe.incontrato.exception.DBException;

public interface OACDAO {

	
	PagoPuntual consultarPagoPuntual(String mensajeTransaccion,String customeId, int numeroMeses) throws DBException;
}
