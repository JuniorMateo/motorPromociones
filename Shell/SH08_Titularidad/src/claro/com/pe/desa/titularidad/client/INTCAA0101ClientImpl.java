package claro.com.pe.desa.titularidad.client;

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

import claro.com.pe.desa.titularidad.client.handler.BssAgreementManagementClientHandler;
import claro.com.pe.desa.titularidad.exception.WSException;
import claro.com.pe.desa.titularidad.util.Constantes;
import claro.com.pe.desa.titularidad.util.EAIUtil;
import claro.com.pe.desa.titularidad.util.JAXBUtilitarios;
import claro.com.pe.desa.titularidad.util.PropertiesExternos;
import pe.com.claro.esb.customer.bsscustomermanagement.v2.BssCustomerManagement;
import pe.com.claro.esb.customer.bsscustomermanagement.v2.BssCustomerManagementPort;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderRequest;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderResponse;
import pe.com.claro.esb.message.bsscustomermanagement.getcustomerinfo.v2.GetCustomerInfoRequestType;
import pe.com.claro.esb.message.bsscustomermanagement.getcustomerinfo.v2.GetCustomerInfoResponseType;
import pe.com.claro.generic.messageformat.v1.HeaderRequestType;
import pe.com.claro.generic.messageformat.v1.HeaderResponseType;
import pe.com.claro.esb.message.bsscustomermanagement.getcustomerinfo.v2.ObjectFactory;

@Service
public class INTCAA0101ClientImpl  implements INTCAA0101Client{
	
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	
	@Autowired
	PropertiesExternos propiedades;
	
	@Override
	public GetCustomerInfoResponseType getCustomerInfo(String mensajeTransaccion,
			GetCustomerInfoRequestType getProductsOfferingPerContractRequestType,
			String usuarioAplicacion, String nombreAplicacion, String idTransaccion, String ipAplicacion)throws Exception {
		
		String metodo = "GetCustomerInfo";
		double tiempoInicio = System.currentTimeMillis();
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo);
		
		ObjectFactory  objectFactory = new ObjectFactory();
		GetCustomerInfoResponseType response =objectFactory.createGetCustomerInfoResponseType();
		
		String URL_WS = propiedades.getProductsOfferingPerContractURL0101;

		Holder<HeaderResponseType> headerResponseType=new Holder<HeaderResponseType>(new HeaderResponseType());

		Holder<HeaderResponse> headerResponse=new Holder<HeaderResponse>(new HeaderResponse());
		
		BssCustomerManagement bssCustomerManagement=new BssCustomerManagement();

		BssCustomerManagementPort bssCustomerManagementPort=bssCustomerManagement.getBssCustomerManagementPort();
		
		
		@SuppressWarnings( "rawtypes" ) 
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add( new BssAgreementManagementClientHandler( propiedades.usernameGetProductsOfferingPerContrac0101, propiedades.passwordGetProductsOfferingPerContract0101 ) );
		Binding binding = ( (BindingProvider)bssCustomerManagementPort ).getBinding();
		binding.setHandlerChain( handlerChain );
		BindingProvider bindingProvider = (BindingProvider)bssCustomerManagementPort;
		bindingProvider.getRequestContext().put( javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, URL_WS );
		bindingProvider.getRequestContext().put( JAXWSProperties.CONNECT_TIMEOUT,Integer.parseInt(propiedades.getProductsOfferingPerContracTimeout0101) );
		bindingProvider.getRequestContext().put( JAXWSProperties.REQUEST_TIMEOUT,Integer.parseInt( propiedades.getProductsOfferingPerContracConnectionTimeout0101) );
		
       try {
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.getTime();
			
			HeaderRequest headerRequest=new HeaderRequest();
			headerRequest.setChannel(propiedades.canalDataPower0101);
			headerRequest.setUserApplication(usuarioAplicacion);
			headerRequest.setUserSession(nombreAplicacion);
			headerRequest.setIdBusinessTransaction(idTransaccion);
			headerRequest.setIdESBTransaction(idTransaccion);
			headerRequest.setIdApplication(ipAplicacion);
			headerRequest.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ) );
			
			HeaderRequestType headerRequestType=new HeaderRequestType();
			headerRequestType.setCountry(propiedades.countryPower0101);
			headerRequestType.setLanguage(propiedades.lenguajePower0101);
			headerRequestType.setConsumer(propiedades.consumerPower0101);
			headerRequestType.setSystem(propiedades.systemPower0101);
			headerRequestType.setModulo(propiedades.moduloPower0101);
			headerRequestType.setPid(idTransaccion);
			headerRequestType.setUserId(propiedades.useridPower0101);
			headerRequestType.setDispositivo(propiedades.dispositivoPower0101);
			headerRequestType.setWsIp(propiedades.wslpPower0101);
			headerRequestType.setOperation(propiedades.operacion1Power0101);
			headerRequestType.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ));
			headerRequestType.setMsgType(propiedades.msgtypePower0101);
			
			LOGGER.info( mensajeTransaccion + "\nDatos de [REQUEST]:\n" 
					+ JAXBUtilitarios.anyObjectToXmlText( headerRequest ) + "\n" 
					+ JAXBUtilitarios.anyObjectToXmlText( headerRequestType ) + "\n" 
					+ JAXBUtilitarios.anyObjectToXmlText( getProductsOfferingPerContractRequestType ) );
			
			response=bssCustomerManagementPort.getCustomerInfo(getProductsOfferingPerContractRequestType,  headerRequest, headerRequestType, headerResponse, headerResponseType);
			
			LOGGER.info( mensajeTransaccion + "\nDatos de [RESPONSE]:\n" 
					+ JAXBUtilitarios.anyObjectToXmlText( response ));
			
		} catch (Exception e) {
			LOGGER.error(mensajeLog + "ERROR: [Exception] 1" + EAIUtil.getStackTraceFromException(e));

			if ( e.toString().contains( SocketTimeoutException.class.toString().replace( "class",Constantes.CONSTANTEVACIA).trim() ) ) {
				LOGGER.error(mensajeTransaccion + "Error producido por timeout en el Servicio "+ propiedades.getProductsOfferingPerContractURL0101);
				 String msjErr = propiedades.consultarClienteIdtmsj3.replace("$ws", Constantes.INTCAA0101).replace("$metodo", Constantes.GETCUSTOMERINFO);
				throw new WSException(propiedades.consultarClienteIdtcod3, msjErr, e);
			}
			
			LOGGER.error(mensajeTransaccion + "ERROR[Exception]: Se produjo una excepcion al ejecutar WS " + propiedades.getProductsOfferingPerContractURL0101);
			String msjError = propiedades.consultarClienteIdtmsj4.replace("$ws", Constantes.INTCAA0101).replace("$metodo", Constantes.GETCUSTOMERINFO);
			throw new WSException(propiedades.consultarClienteIdtcod4, msjError, e);
		} finally {
			LOGGER.info(mensajeLog + "[FIN] - METODO: " + metodo);
			LOGGER.info(mensajeLog + " ::FIN - TiempoEjecucion(ms): "+(System.currentTimeMillis()-tiempoInicio));
		}
		
		   return response;
	}


}
