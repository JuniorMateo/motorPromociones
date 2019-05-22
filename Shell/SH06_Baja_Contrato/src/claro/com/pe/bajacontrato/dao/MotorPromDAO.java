package claro.com.pe.bajacontrato.dao;

import claro.com.pe.bajacontrato.bean.LisBonoProgram;
import claro.com.pe.bajacontrato.bean.ResponseAuditoria;
import claro.com.pe.bajacontrato.exception.DBException;


public interface MotorPromDAO {

	
	public LisBonoProgram obtenerBonosPorBajaContrato(String mensajeTransaccion)
			throws  DBException;

    ResponseAuditoria actualizarBono(String mensajeTransaccion,String programid,String observacion)
			throws DBException;
}
