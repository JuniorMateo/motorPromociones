package claro.com.pe.desa.titularidad.dao;

import claro.com.pe.desa.titularidad.bean.ListaBono;
import claro.com.pe.desa.titularidad.bean.ResponseAuditoria;
import claro.com.pe.desa.titularidad.exception.DBException;


public interface MotorPromDAO {
	
	ListaBono obtenerBonosAdesactivarTitularidad(String mensajeTransaccion,int tipoDesactivacion)throws DBException;
	 ResponseAuditoria actualizarBono(String mensajeTransaccion, String programid,String observacion)
			throws DBException;
		
}