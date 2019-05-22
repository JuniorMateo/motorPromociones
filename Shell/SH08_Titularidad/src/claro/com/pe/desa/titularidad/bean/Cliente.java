package claro.com.pe.desa.titularidad.bean;

import java.io.Serializable;

public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;
	private String dni;
	private String tipoDocument;
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getTipoDocument() {
		return tipoDocument;
	}
	public void setTipoDocument(String tipoDocument) {
		this.tipoDocument = tipoDocument;
	}
	

}
