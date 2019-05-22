package claro.com.pe.carga.datos.replica.cambio.plan.dao;

import claro.com.pe.carga.datos.replica.cambio.plan.bean.LisBonoProgram;
import claro.com.pe.carga.datos.replica.cambio.plan.bean.ResponseAuditoria;
import claro.com.pe.carga.datos.replica.cambio.plan.exception.DBException;

public interface MotorPromDAO {

	
	 LisBonoProgram obtenerConsCambioplan(String mensajeTransaccion,String fechaConsulta)
			throws  DBException;
	
		
	 ResponseAuditoria insertarCargaMasiva(String mensajeTransaccion, LisBonoProgram lista)
			throws  DBException;
}
