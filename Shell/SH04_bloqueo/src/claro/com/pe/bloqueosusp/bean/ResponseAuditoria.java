package claro.com.pe.bloqueosusp.bean;

import java.io.Serializable;

public class ResponseAuditoria implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private String codigoRespuesta;
	private String mensajeRespuesta;
	
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}
	
	public String getMensajeRespuesta() {
		return mensajeRespuesta;
	}
	public void setMensajeRespuesta(String mensajeRespuesta) {
		this.mensajeRespuesta = mensajeRespuesta;
	}

}
