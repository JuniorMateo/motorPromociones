package claro.com.pe.cambioplan.bean;

import java.io.Serializable;
import java.util.List;

public class LisBonoProgram implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private List<BonoProgram> listabono;
	private String codigoError;
	private String descripcionError;
	
	public List<BonoProgram> getListabono() {
		return listabono;
	}
	public void setListabono(List<BonoProgram> listabono) {
		this.listabono = listabono;
	}
	public String getCodigoError() {
		return codigoError;
	}
	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}
	public String getDescripcionError() {
		return descripcionError;
	}
	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}
	
	
	 

}
