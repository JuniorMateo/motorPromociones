package claro.com.pe.bloqueosusp.dao;

import claro.com.pe.bloqueosusp.bean.LisBonoProgram;
import claro.com.pe.bloqueosusp.bean.ResponseAuditoria;
import claro.com.pe.bloqueosusp.exception.DBException;

public interface MotorPromDAO {

    LisBonoProgram obtenerBonosPorSuspencion(String mensajeTransaccion,String criterio)
			throws  DBException;

    ResponseAuditoria actualizarBono(String mensajeTransaccion, String piProgramId,String observacion)
			throws DBException;
		
}
