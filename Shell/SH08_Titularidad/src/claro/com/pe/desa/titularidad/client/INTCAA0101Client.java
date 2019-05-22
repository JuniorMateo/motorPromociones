package claro.com.pe.desa.titularidad.client;

import pe.com.claro.esb.message.bsscustomermanagement.getcustomerinfo.v2.GetCustomerInfoRequestType;
import pe.com.claro.esb.message.bsscustomermanagement.getcustomerinfo.v2.GetCustomerInfoResponseType;

public interface INTCAA0101Client {

	
	public  GetCustomerInfoResponseType getCustomerInfo(String mensajeTransaccion,
			GetCustomerInfoRequestType GetCustomerInfoRequestType,
			String usuarioAplicacion,String nombreAplicacion,String idTransaccion,String ipAplicacion) throws Exception;
}
