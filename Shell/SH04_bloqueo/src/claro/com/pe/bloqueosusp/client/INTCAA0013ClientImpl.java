package claro.com.pe.bloqueosusp.client;

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

import claro.com.pe.bloqueosusp.client.handler.BssAgreementManagementClientHandler;
import claro.com.pe.bloqueosusp.exception.WSException;
import claro.com.pe.bloqueosusp.util.Constantes;
import claro.com.pe.bloqueosusp.util.EAIUtil;
import claro.com.pe.bloqueosusp.util.JAXBUtilitarios;
import claro.com.pe.bloqueosusp.util.PropertiesExternos;
import pe.com.claro.esb.customer.bsscustomermanagement.v2.BssCustomerManagement;
import pe.com.claro.esb.customer.bsscustomermanagement.v2.BssCustomerManagementPort;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderRequest;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderResponse;
import pe.com.claro.esb.message.bsscustomermanagement.getstatusreasons.v2.GetStatusReasonsRequestType;
import pe.com.claro.esb.message.bsscustomermanagement.getstatusreasons.v2.GetStatusReasonsResponseType;
import pe.com.claro.generic.messageformat.v1.HeaderRequestType;
import pe.com.claro.generic.messageformat.v1.HeaderResponseType;
import pe.com.claro.esb.message.bsscustomermanagement.getstatusreasons.v2.ObjectFactory;

@Service
public class INTCAA0013ClientImpl implements INTCAA0013Client{

	
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	
	@Autowired
	PropertiesExternos propiedades;


	@Override
	public GetStatusReasonsResponseType getStatusReasons(GetStatusReasonsRequestType request, String mensajeTransaccion,
			String usuarioAplicacion, String nombreAplicacion, String idTransaccion, String ipAplicacion)
			throws Exception {
		String URL_WS = propiedades.getStatusReasons0013URL;
		String metodo = "getStatusReasons";
		double tiempoInicio = System.currentTimeMillis();
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo+ "  WS URL: "+URL_WS);
		
		ObjectFactory  objectFactory = new ObjectFactory();
		GetStatusReasonsResponseType response =objectFactory.createGetStatusReasonsResponseType();
		


		Holder<HeaderResponseType> headerResponseType=new Holder<HeaderResponseType>(new HeaderResponseType());

		Holder<HeaderResponse> headerResponse=new Holder<HeaderResponse>(new HeaderResponse());
		
		BssCustomerManagement  BssCustomerManagement=new  BssCustomerManagement();

		BssCustomerManagementPort bssCustomerManagementPort=BssCustomerManagement.getBssCustomerManagementPort();
		
		@SuppressWarnings( "rawtypes" ) 
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add( new BssAgreementManagementClientHandler( propiedades.usernameStatusReasons0013, propiedades.passwordStatusReasons0013 ) );
		Binding binding = ( (BindingProvider)bssCustomerManagementPort ).getBinding();
		binding.setHandlerChain( handlerChain );
		BindingProvider bindingProvider = (BindingProvider)bssCustomerManagementPort;
		bindingProvider.getRequestContext().put( javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, URL_WS );
		bindingProvider.getRequestContext().put( JAXWSProperties.CONNECT_TIMEOUT,Integer.parseInt(propiedades.getStatusReasons0013ConnectionTimeout) );
		bindingProvider.getRequestContext().put( JAXWSProperties.REQUEST_TIMEOUT,Integer.parseInt( propiedades.getStatusReasons0013Timeout) );
		
		 try {
				
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.getTime();
				
				HeaderRequest headerRequest=new HeaderRequest();
				headerRequest.setChannel(propiedades.canalStatusReasons0013);
				headerRequest.setUserApplication(usuarioAplicacion);
				headerRequest.setUserSession(nombreAplicacion);
				headerRequest.setIdBusinessTransaction(idTransaccion);
				headerRequest.setIdESBTransaction(idTransaccion);
				headerRequest.setIdApplication(ipAplicacion);
				headerRequest.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ) );
				
				HeaderRequestType headerRequestType=new HeaderRequestType();
				headerRequestType.setCountry(propiedades.countryStatusReasons0013);
				headerRequestType.setLanguage(propiedades.lenguajeStatusReasons0013);
				headerRequestType.setConsumer(propiedades.consumerStatusReasons0013);
				headerRequestType.setSystem(propiedades.systemStatusReasons0013);
				headerRequestType.setModulo(propiedades.moduloStatusReasons0013);
				headerRequestType.setPid(idTransaccion);
				headerRequestType.setUserId(propiedades.useridStatusReasons0013);
				headerRequestType.setDispositivo(propiedades.dispositivoStatusReasons0013);
				headerRequestType.setWsIp(propiedades.wslpStatusReasons0013);
				headerRequestType.setOperation(propiedades.operacion1StatusReasons0013);
				headerRequestType.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ));
				headerRequestType.setMsgType(propiedades.msgtypeStatusReasons0013);
				
				LOGGER.info( mensajeTransaccion + "\nDatos de [REQUEST]:\n" 
						+ JAXBUtilitarios.anyObjectToXmlText( headerRequest ) + "\n" 
						+ JAXBUtilitarios.anyObjectToXmlText( headerRequestType ) + "\n" 
						+ JAXBUtilitarios.anyObjectToXmlText( request ) );

				response=bssCustomerManagementPort.getStatusReasons(request,headerRequest, headerRequestType, headerResponse, headerResponseType);
				
				LOGGER.info( mensajeTransaccion + "\nDatos de [RESPONSE]:\n" 
						+ JAXBUtilitarios.anyObjectToXmlText( response ) );
			} catch (Exception e) {
				LOGGER.error(mensajeLog + "ERROR: [Exception] 1" + EAIUtil.getStackTraceFromException(e));

				if ( e.toString().contains( SocketTimeoutException.class.toString().replace( "class",Constantes.CONSTANTEVACIA).trim() ) ) {
					LOGGER.error(mensajeTransaccion + "Error producido por timeout en el Servicio "+ URL_WS);
					 String msjErr = propiedades.MSJ_IDT3.replace("$ws", Constantes.INTCAA0013).replace("$metodo", Constantes.GETSTATUSREASONS);
					throw new WSException(propiedades.COD_IDT3, msjErr, e);
				}
				
				LOGGER.error(mensajeTransaccion + "ERROR[Exception]: Se produjo una excepcion al ejecutar WS " + URL_WS);
				String msjError = propiedades.MSJ_IDT4.replace("$ws", Constantes.INTCAA0013).replace("$metodo", Constantes.GETSTATUSREASONS);
				throw new WSException(propiedades.COD_IDT4, msjError, e);
			} finally {
				LOGGER.info(mensajeLog + "[FIN] - METODO: " + metodo);
				LOGGER.info(mensajeLog + " ::FIN - TiempoEjecucion(ms): "+(System.currentTimeMillis()-tiempoInicio));
			}
			
		
		return response;
	}
}
