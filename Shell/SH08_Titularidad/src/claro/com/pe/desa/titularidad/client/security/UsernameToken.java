package claro.com.pe.desa.titularidad.client.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "UsernameToken", namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" )
public class UsernameToken{

	@XmlElement( name = "Username" )
	private String	username;

	@XmlElement( name = "Password" )
	private String	password;

	public UsernameToken(){
		super();
	}

	public UsernameToken( String username, String password ){
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

}
