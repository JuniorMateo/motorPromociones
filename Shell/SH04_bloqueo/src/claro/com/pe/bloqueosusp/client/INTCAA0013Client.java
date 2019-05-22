package claro.com.pe.bloqueosusp.client;

import pe.com.claro.esb.message.bsscustomermanagement.getstatusreasons.v2.GetStatusReasonsRequestType;
import pe.com.claro.esb.message.bsscustomermanagement.getstatusreasons.v2.GetStatusReasonsResponseType;

public interface INTCAA0013Client {
	
	public GetStatusReasonsResponseType getStatusReasons(GetStatusReasonsRequestType request,String mensajeTransaccion,
			String usuarioAplicacion,String nombreAplicacion,String idTransaccion,String ipAplicacion) throws Exception;

}
