package claro.com.pe.cambioplan.client;

import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractRequestType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractResponseType;

public interface INTCAA0002Client {

	
	public  GetProductsOfferingPerContractResponseType GetProductsOfferingPerContractResponse(String mensajeTransaccion,
			GetProductsOfferingPerContractRequestType getProductsOfferingPerContractRequestType,
			String usuarioAplicacion,String nombreAplicacion,String idTransaccion,String ipAplicacion) throws Exception;
}
