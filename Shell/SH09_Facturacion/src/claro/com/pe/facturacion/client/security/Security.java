package claro.com.pe.facturacion.client.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "Security", namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" )
public class Security{

	@XmlElement( name = "UsernameToken" )
	private UsernameToken	usernameToken;

	public Security(){
		super();
	}

	public Security( UsernameToken usernameToken ){
		super();
		this.usernameToken = usernameToken;
	}

	public UsernameToken getUsernameToken(){
		return usernameToken;
	}

	public void setUsernameToken( UsernameToken usernameToken ){
		this.usernameToken = usernameToken;
	}

}
