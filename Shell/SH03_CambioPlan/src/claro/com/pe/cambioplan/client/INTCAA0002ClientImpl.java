package claro.com.pe.cambioplan.client;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.xml.internal.ws.developer.JAXWSProperties;

import claro.com.pe.cambioplan.client.handler.BssProductOrderingManzgementClientHandler;
import claro.com.pe.cambioplan.exception.WSException;
import claro.com.pe.cambioplan.util.Constantes;
import claro.com.pe.cambioplan.util.EAIUtil;
import claro.com.pe.cambioplan.util.JAXBUtilitarios;
import claro.com.pe.cambioplan.util.PropertiesExternos;
import pe.com.claro.esb.customer.bssproductordering.v2.BssProductOrdering;
import pe.com.claro.esb.customer.bssproductordering.v2.BssProductOrderingPort;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderRequest;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderResponse;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractRequestType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractResponseType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.ObjectFactory;
import pe.com.claro.generic.messageformat.v1.HeaderRequestType;
import pe.com.claro.generic.messageformat.v1.HeaderResponseType;

@Service
public class INTCAA0002ClientImpl  implements INTCAA0002Client{
	
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	
	@Autowired
	PropertiesExternos propiedades;
	
	@Override
	public GetProductsOfferingPerContractResponseType GetProductsOfferingPerContractResponse(String mensajeTransaccion,
			GetProductsOfferingPerContractRequestType getProductsOfferingPerContractRequestType,
			String usuarioAplicacion, String nombreAplicacion, String idTransaccion, String ipAplicacion)throws Exception {
		
		String metodo = "GetProductsOfferingPerContract";
		double tiempoInicio = System.currentTimeMillis();
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo);
		
		ObjectFactory  objectFactory = new ObjectFactory();
		GetProductsOfferingPerContractResponseType response =objectFactory.createGetProductsOfferingPerContractResponseType();
		
		String URL_WS = propiedades.getProductsOfferingPerContractURL;

		Holder<HeaderResponseType> headerResponseType=new Holder<HeaderResponseType>(new HeaderResponseType());

		Holder<HeaderResponse> headerResponse=new Holder<HeaderResponse>(new HeaderResponse());
		
		BssProductOrdering getProductsOfferingPerContractService=new BssProductOrdering();

		BssProductOrderingPort getProductsOfferingPerContractPort=getProductsOfferingPerContractService.getBssProductOrderingPort();
		
		
		@SuppressWarnings( "rawtypes" ) 
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add( new BssProductOrderingManzgementClientHandler( propiedades.usernameGetProductsOfferingPerContrac, propiedades.passwordGetProductsOfferingPerContract ) );
		Binding binding = ( (BindingProvider)getProductsOfferingPerContractPort ).getBinding();
		binding.setHandlerChain( handlerChain );
		BindingProvider bindingProvider = (BindingProvider)getProductsOfferingPerContractPort;
		bindingProvider.getRequestContext().put( javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, URL_WS );
		bindingProvider.getRequestContext().put( JAXWSProperties.CONNECT_TIMEOUT,Integer.parseInt(propiedades.getProductsOfferingPerContracConnectionTimeout) );
		bindingProvider.getRequestContext().put( JAXWSProperties.REQUEST_TIMEOUT,Integer.parseInt( propiedades.getProductsOfferingPerContracTimeout) );
		
       try {
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.getTime();
			
			HeaderRequest headerRequest=new HeaderRequest();
			headerRequest.setChannel(propiedades.canalDataPower);
			headerRequest.setUserApplication(usuarioAplicacion);
			headerRequest.setUserSession(nombreAplicacion);
			headerRequest.setIdBusinessTransaction(idTransaccion);
			headerRequest.setIdESBTransaction(idTransaccion);
			headerRequest.setIdApplication(ipAplicacion);
			headerRequest.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ) );
			
			HeaderRequestType headerRequestType=new HeaderRequestType();
			headerRequestType.setCountry(propiedades.countryPower);
			headerRequestType.setLanguage(propiedades.lenguajePower);
			headerRequestType.setConsumer(propiedades.consumerPower);
			headerRequestType.setSystem(propiedades.systemPower);
			headerRequestType.setModulo(propiedades.moduloPower);
			headerRequestType.setPid(idTransaccion);
			headerRequestType.setUserId(propiedades.useridPower);
			headerRequestType.setDispositivo(propiedades.dispositivoPower);
			headerRequestType.setWsIp(propiedades.wslpPower);
			headerRequestType.setOperation(propiedades.operacion1Power);
			headerRequestType.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ));
			headerRequestType.setMsgType(propiedades.msgtypePower);
			
			LOGGER.info( mensajeTransaccion + "\nDatos de [REQUEST]:\n" 
					+ JAXBUtilitarios.anyObjectToXmlText( headerRequest ) + "\n" 
					+ JAXBUtilitarios.anyObjectToXmlText( headerRequestType ) + "\n" 
					+ JAXBUtilitarios.anyObjectToXmlText( getProductsOfferingPerContractRequestType ) );
			
			response=getProductsOfferingPerContractPort.getProductsOfferingPerContract(getProductsOfferingPerContractRequestType, headerRequest, headerRequestType, headerResponse, headerResponseType);
			
			
			LOGGER.info( "{} XML [RESPONSE]: response: [{}]" + mensajeTransaccion + "\n" + JAXBUtilitarios.anyObjectToXmlText( response ) );
		} catch (Exception e) {
			LOGGER.error(mensajeLog + "ERROR: [Exception] 1" + EAIUtil.getStackTraceFromException(e));

			if ( e.toString().contains( SocketTimeoutException.class.toString().replace( "class",Constantes.CADENAVACIA).trim() ) ) {
				LOGGER.error(mensajeTransaccion + "Error producido por timeout en el Servicio "+ propiedades.getProductsOfferingPerContractURL);
				 String msjErr = propiedades.consultarClienteIdtmsj3.replace("$ws", Constantes.INTCAA0002).replace("$metodo", Constantes.GETPRODUCTSOFFERINGPERCONTRACT);
				throw new WSException(propiedades.consultarClienteIdtcod3, msjErr, e);
			}
			
			LOGGER.error(mensajeTransaccion + "ERROR[Exception]: Se produjo una excepcion al ejecutar WS " + propiedades.getProductsOfferingPerContractURL);
			String msjError = propiedades.consultarClienteIdtmsj4.replace("$ws", Constantes.INTCAA0002).replace("$metodo", Constantes.GETPRODUCTSOFFERINGPERCONTRACT);
			throw new WSException(propiedades.consultarClienteIdtcod4, msjError, e);
		} finally {
			LOGGER.info(mensajeLog + "[FIN] - METODO: " + metodo);
			LOGGER.info(mensajeLog + " ::FIN - TiempoEjecucion(ms): "+(System.currentTimeMillis()-tiempoInicio));
		}
		
		   return response;
	}


}