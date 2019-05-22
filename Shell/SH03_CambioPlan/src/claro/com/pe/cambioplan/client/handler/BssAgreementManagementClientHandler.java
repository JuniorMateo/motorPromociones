package claro.com.pe.cambioplan.client.handler;

import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.log4j.Logger;

import claro.com.pe.cambioplan.client.security.Security;
import claro.com.pe.cambioplan.client.security.UsernameToken;

public class BssAgreementManagementClientHandler implements SOAPHandler<SOAPMessageContext>{

	private final Logger LOGGER = Logger.getLogger(BssAgreementManagementClientHandler.class);

	private String				username;
	private String				password;

	public BssAgreementManagementClientHandler(){
		super();
	}

	public BssAgreementManagementClientHandler( String username, String password ){
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername(){
		return username;
	}

	public void setUsername( String username ){
		this.username = username;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword( String password ){
		this.password = password;
	}

	@Override
	public boolean handleMessage( SOAPMessageContext context ){
		Boolean isRequest = (Boolean)context.get( MessageContext.MESSAGE_OUTBOUND_PROPERTY );
		try{
			// ES REQUEST
			if( isRequest ){
				LOGGER.info( "Agregando credenciales de autenticacion en el Header..." );
				SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader() == null? envelope.addHeader(): envelope.getHeader();

				JAXBContext jaxbContext = JAXBContext.newInstance( Security.class );
				Marshaller marshaller = jaxbContext.createMarshaller();

				UsernameToken userNameToken = new UsernameToken();
				userNameToken.setUsername( username );
				userNameToken.setPassword( password );

				Security headerObj = new Security();
				headerObj.setUsernameToken( userNameToken );

				marshaller.marshal( headerObj, header );
				
				LOGGER.info( "Credenciales de autenticacion agregadas. Se continua con la invocacion" );
				
				return true;
			}

		}
		catch( Exception e ){
			LOGGER.error( "Error al agregar credenciales de autenticacion en el Header", e );
			return false;
		}
		return true;

	}

	@Override
	public boolean handleFault( SOAPMessageContext context ){
		LOGGER.info( "Se detecto un Fault en el Handler..." );

		return handleMessage( context );
	}

	@Override
	public void close( MessageContext context ){
		// TODO Auto-generated method stub

	}

	@Override
	public Set<QName> getHeaders(){
		// TODO Auto-generated method stub
		return null;
	}

}
