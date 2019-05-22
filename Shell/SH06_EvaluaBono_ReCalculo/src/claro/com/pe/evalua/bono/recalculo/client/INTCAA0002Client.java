package claro.com.pe.evalua.bono.recalculo.client;

import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractRequestType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractResponseType;

public interface INTCAA0002Client {

	
	public  GetProductsOfferingPerContractResponseType GetProductsOfferingPerContractResponse(String mensajeTransaccion,
			GetProductsOfferingPerContractRequestType getProductsOfferingPerContractRequestType,
			String usuarioAplicacion,String nombreAplicacion,String idTransaccion) throws Exception;
}
