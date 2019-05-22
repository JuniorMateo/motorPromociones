package claro.com.pe.facturacion.dao;

import claro.com.pe.facturacion.bean.LisBonoProgram;
import claro.com.pe.facturacion.bean.ResponseAuditoria;
import claro.com.pe.facturacion.exception.DBException;
public interface MotorPromDAO {

	
	 LisBonoProgram obtenerCambioFacturacion(String mensajeTransaccion)
			throws  DBException;
		
	 ResponseAuditoria actualizarBono(String mensajeTransaccion,   String programid,String observacion)
			throws  DBException;
}
