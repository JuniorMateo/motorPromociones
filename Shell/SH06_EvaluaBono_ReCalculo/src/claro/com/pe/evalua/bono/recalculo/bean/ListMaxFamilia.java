package claro.com.pe.evalua.bono.recalculo.bean;

import java.io.Serializable;
import java.util.List;

public class ListMaxFamilia implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private List<BonoProgram> listaMax;
	private String codigoError;
	private String descripcionError;
	
	
	public List<BonoProgram> getListaMax() {
		return listaMax;
	}
	public void setListaMax(List<BonoProgram> listaMax) {
		this.listaMax = listaMax;
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
