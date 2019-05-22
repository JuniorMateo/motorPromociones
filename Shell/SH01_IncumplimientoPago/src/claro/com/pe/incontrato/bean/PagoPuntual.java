package claro.com.pe.incontrato.bean;

import java.io.Serializable;

public class PagoPuntual implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int indicador;
	private String codigoRespuesta;
	private String mensajeRespuesta;

	
	public int getIndicador() {
		return indicador;
	}
	public void setIndicador(int indicador) {
		this.indicador = indicador;
	}
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
