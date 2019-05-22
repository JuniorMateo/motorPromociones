package claro.com.pe.cambioplan.dao;
import claro.com.pe.cambioplan.bean.LisBonoProgram;
import claro.com.pe.cambioplan.bean.ResponseAuditoria;
import claro.com.pe.cambioplan.exception.DBException;
import claro.com.pe.cambioplan.exception.DBNoDisponibleException;
import claro.com.pe.cambioplan.exception.DBTimeoutException;


public interface MotorPromDAO {
	
   public LisBonoProgram obtenerCambioPlan(String mensajeTransaccion,String pi_fec_proc,String pi_dias,int tipoDesactivacion)
		   throws DBTimeoutException, DBNoDisponibleException, DBException;

   public ResponseAuditoria actualizarBono(String mensajeTransaccion,String programid,String observacion)
		   throws DBTimeoutException, DBNoDisponibleException, DBException;
   
   public ResponseAuditoria actualizarCambioPlanNoMatriz(String mensajeTransaccion,String programid,String pi_campania,String piBasic,String Des)
		   throws DBTimeoutException, DBNoDisponibleException, DBException;
   
   public ResponseAuditoria ejecutarCambioPlanPrepago(String mensajeTransaccion,String pi_fec_proc,String pi_dias)
		   throws DBTimeoutException, DBNoDisponibleException, DBException;

   public LisBonoProgram CambioPlanNoMatriz(String mensajeTransaccion,String pi_fec_proc,String pi_dias,int tipoDesactivacion)
		   throws  DBException;
  
}
