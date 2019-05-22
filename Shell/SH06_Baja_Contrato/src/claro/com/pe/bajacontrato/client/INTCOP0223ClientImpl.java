package claro.com.pe.bajacontrato.client;

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

import claro.com.pe.bajacontrato.exception.WSException;
import claro.com.pe.bajacontrato.handler.BssAgreementManagementClientHandler;
import claro.com.pe.bajacontrato.util.Constantes;
import claro.com.pe.bajacontrato.util.EAIUtil;
import claro.com.pe.bajacontrato.util.JAXBUtilitarios;
import claro.com.pe.bajacontrato.util.PropertiesExternos;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderRequest;
import pe.com.claro.esb.data.commonbusinessentities.clarogenericheaders.v2.HeaderResponse;
import pe.com.claro.esb.engagedparty.bssagreementmanagement.v2.BssAgreementManagement;
import pe.com.claro.esb.engagedparty.bssagreementmanagement.v2.BssAgreementManagementPort;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsRequestType;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsResponseType;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.ObjectFactory;
import pe.com.claro.generic.messageformat.v1.HeaderRequestType;
import pe.com.claro.generic.messageformat.v1.HeaderResponseType;

@Service
public class INTCOP0223ClientImpl implements INTCOP0223Client  {
	
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	@Autowired
	PropertiesExternos propiedades;

	@Override
	public RetrieveSubscriptionsResponseType retrieveSubscriptionsResponse(String mensajeTransaccion,RetrieveSubscriptionsRequestType retrieveSubscriptionsRequestType,
			String usuarioAplicacion, String nombreAplicacion, String idTransaccion, String ipAplicacion) throws Exception{
		
		String metodo = "retrieveSubscriptions";
		double tiempoInicio = System.currentTimeMillis();
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		String URL_WS = propiedades.bssAgreementManagementURL;
		LOGGER.info(mensajeLog + "[INICIO] - WS: " + URL_WS);
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo);
		
		ObjectFactory  objectFactory = new ObjectFactory();
		RetrieveSubscriptionsResponseType response=objectFactory.createRetrieveSubscriptionsResponseType();
		Holder<HeaderResponseType> headerResponseType=new Holder<HeaderResponseType>(new HeaderResponseType());

		
		Holder<HeaderResponse> headerResponse=new Holder<HeaderResponse>(new HeaderResponse());
		BssAgreementManagement  bssAgreementManagement=new BssAgreementManagement();
		BssAgreementManagementPort bssAgreementManagementPort=bssAgreementManagement.getBssAgreementManagementPort();
		
		@SuppressWarnings( "rawtypes" ) 
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add( new BssAgreementManagementClientHandler( propiedades.usernameBssAgreementManagement, propiedades.passwordBssAgreementManagement ) );
		Binding binding = ( (BindingProvider)bssAgreementManagementPort ).getBinding();
		binding.setHandlerChain( handlerChain );
		BindingProvider bindingProvider = (BindingProvider)bssAgreementManagementPort;
		bindingProvider.getRequestContext().put( javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, URL_WS );
		bindingProvider.getRequestContext().put( JAXWSProperties.CONNECT_TIMEOUT,Integer.parseInt(propiedades.bssAgreementManagementConnectionTimeout) );
		bindingProvider.getRequestContext().put( JAXWSProperties.REQUEST_TIMEOUT,Integer.parseInt( propiedades.bssAgreementManagementTimeout) );
		
		
		 try {
				
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.getTime();
				
				HeaderRequest headerRequest=new HeaderRequest();
				headerRequest.setChannel(propiedades.canalbssAgreementManagement);
				headerRequest.setUserApplication(usuarioAplicacion);
				headerRequest.setUserSession(nombreAplicacion);
				headerRequest.setIdBusinessTransaction(idTransaccion);
				headerRequest.setIdESBTransaction(idTransaccion);
				headerRequest.setIdApplication(ipAplicacion);
				headerRequest.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ) );
				
				HeaderRequestType headerRequestType=new HeaderRequestType();
				headerRequestType.setCountry(propiedades.countrybssAgreementManagementr);
				headerRequestType.setLanguage(propiedades.lenguajebssAgreementManagementr);
				headerRequestType.setConsumer(propiedades.consumerbssAgreementManagementr);
				headerRequestType.setSystem(propiedades.systembssAgreementManagement);
				headerRequestType.setModulo(propiedades.moduloBssAgreementManagement);
				headerRequestType.setPid(idTransaccion);
				headerRequestType.setUserId(propiedades.useridBssAgreementManagement);
				headerRequestType.setDispositivo(propiedades.dispositivoBssAgreementManagement);
				headerRequestType.setWsIp(propiedades.wslpBssAgreementManagement);
				headerRequestType.setOperation(propiedades.operacion1BssAgreementManagement);
				headerRequestType.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar ));
				headerRequestType.setMsgType(propiedades.msgtypePower);
				
				LOGGER.info( mensajeTransaccion + "\nDatos de [REQUEST]:\n" 
						+ JAXBUtilitarios.anyObjectToXmlText( headerRequest ) + "\n" 
						+ JAXBUtilitarios.anyObjectToXmlText( headerRequestType ) + "\n" 
						+ JAXBUtilitarios.anyObjectToXmlText( retrieveSubscriptionsRequestType ) );
				
				response=bssAgreementManagementPort.retrieveSubscriptions(retrieveSubscriptionsRequestType, headerRequest, headerRequestType, headerResponse, headerResponseType);
				LOGGER.info( "{} XML [RESPONSE]: response: [{}]" + mensajeTransaccion + "\n" + JAXBUtilitarios.anyObjectToXmlText( response ) );
			} catch (Exception e) {
				LOGGER.error(mensajeLog + "ERROR: [Exception] 1" + EAIUtil.getStackTraceFromException(e));

				if ( e.toString().contains( SocketTimeoutException.class.toString().replace( "class",Constantes.CADENAVACIA).trim() ) ) {
					LOGGER.error(mensajeTransaccion + "Error producido por timeout en el Servicio "+ propiedades.bssAgreementManagementURL);
					 String msjErr = propiedades.consultarClienteIdtmsj3.replace("$ws", Constantes.INTCOP0223).replace("$metodo", Constantes.RETRIEVESUBSCRIPTIONS);
					throw new WSException(propiedades.consultarClienteIdtcod3, msjErr, e);
				}
				
				LOGGER.error(mensajeTransaccion + "ERROR[Exception]: Se produjo una excepcion al ejecutar WS " + propiedades.bssAgreementManagementURL);
				String msjError = propiedades.consultarClienteIdtmsj4.replace("$ws", Constantes.INTCOP0223).replace("$metodo", Constantes.RETRIEVESUBSCRIPTIONS);
				throw new WSException(propiedades.consultarClienteIdtcod4, msjError, e);
			} finally {
				LOGGER.info(mensajeLog + "[FIN] - METODO: " + metodo);
				LOGGER.info(mensajeLog + " ::FIN - TiempoEjecucion(ms): "+(System.currentTimeMillis()-tiempoInicio));
			}
		return response;
	}

	
}
