package claro.com.pe.facturacion.client;

import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsRequestType;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsResponseType;

public interface INTCOP0223Client {

	RetrieveSubscriptionsResponseType retrieveSubscriptionsResponse(String mensajeTransaccion,RetrieveSubscriptionsRequestType retrieveSubscriptionsRequestType,
			String usuarioAplicacion, String nombreAplicacion, String idTransaccion, String ipAplicacion)throws Exception;

}
