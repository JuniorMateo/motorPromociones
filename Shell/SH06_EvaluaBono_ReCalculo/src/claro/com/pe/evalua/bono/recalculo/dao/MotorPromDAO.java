package claro.com.pe.evalua.bono.recalculo.dao;

import java.util.List;

import claro.com.pe.evalua.bono.recalculo.bean.BonoProgram;
import claro.com.pe.evalua.bono.recalculo.bean.BonoRecalcula;
import claro.com.pe.evalua.bono.recalculo.bean.LisBonoProgram;
import claro.com.pe.evalua.bono.recalculo.bean.ListMaxFamilia;
import claro.com.pe.evalua.bono.recalculo.bean.ListaBonoRecalculo;
import claro.com.pe.evalua.bono.recalculo.bean.ResponseAuditoria;
import claro.com.pe.evalua.bono.recalculo.bean.Subscriptions;
import claro.com.pe.evalua.bono.recalculo.exception.DBException;

public interface MotorPromDAO {

	
	 LisBonoProgram obtenerRecalcularEvaluacion(String mensajeTransaccion)
			throws  DBException;
	
		
	 ResponseAuditoria insertarBonoInterno(String mensajeTransaccion, BonoRecalcula lista)
			throws  DBException;
	 
	 ListMaxFamilia getObtenerRecalculo(String mensajeTransaccion,List< Subscriptions> subscriptions,int num)
				throws DBException;
	 
	 ResponseAuditoria actualizarBono(String mensajeTransaccion,String program_id,String observacion)
			   throws  DBException;
	 
	 ListaBonoRecalculo obtenerBonoInfoRecalculo(String mensajeTransaccion,String plan,String motivo,String campania)
				throws  DBException;
	 
	 ResponseAuditoria insertarContratoInterno(String mensajeTransaccion, BonoProgram bono)
				throws DBException;
}
