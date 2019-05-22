package claro.com.pe.incontrato.dao;


import claro.com.pe.incontrato.bean.LisBonoProgram;
import claro.com.pe.incontrato.bean.ResponseAuditoria;
import claro.com.pe.incontrato.exception.DBException;


public interface MotorPromDAO {

	
		
	LisBonoProgram obtenerCambioFacturacion(String mensajeTransaccion)
			throws  DBException; 
    ResponseAuditoria actualizarBono(String mensajeTransaccion, String program_id,String observacion)
			throws DBException;
}