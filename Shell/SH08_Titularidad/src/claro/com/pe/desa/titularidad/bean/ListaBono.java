package claro.com.pe.desa.titularidad.bean;

import java.io.Serializable;
import java.util.List;

public class ListaBono implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String codigoRespuesta;
	private String mensajeRespuesta;
	private List<Bono> listabono;
	
	public List<Bono> getListabono() {
		return listabono;
	}
	public void setListabono(List<Bono> listabono) {
		this.listabono = listabono;
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
