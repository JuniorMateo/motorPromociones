package claro.com.pe.desa.titularidad.bean;

import java.io.Serializable;

public class Bono implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String linea;
	private String numerodeDocumento;
	private String programid;
	private String tipodocu;

	public String getTipodocu() {
		return tipodocu;
	}
	public void setTipodocu(String tipodocu) {
		this.tipodocu = tipodocu;
	}
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public String getNumerodeDocumento() {
		return numerodeDocumento;
	}
	public void setNumerodeDocumento(String numerodeDocumento) {
		this.numerodeDocumento = numerodeDocumento;
	}
	public String getProgramid() {
		return programid;
	}
	public void setProgramid(String programid) {
		this.programid = programid;
	}
	

}
