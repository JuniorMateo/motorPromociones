package claro.com.pe.validavigencia.dao;

import claro.com.pe.validavigencia.bean.ResponseAuditoria;
import claro.com.pe.validavigencia.exception.DBException;

public interface MotorPromDAO {

	
	public ResponseAuditoria  ejecutarTransaccionFechaVigencia(String mensajeTransaccion, String pi_fec_proc, String pi_dias,String observacion) throws DBException;
		
}
