package claro.com.pe.evalua.bono.recalculo.bean;

import java.io.Serializable;
import java.util.List;

public class ListaBonoRecalculo implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private List<BonoRecalculo> listaRecalculo;
	private String codigoError;
	private String descripcionError;
	public List<BonoRecalculo> getListaRecalculo() {
		return listaRecalculo;
	}
	public void setListaRecalculo(List<BonoRecalculo> listaRecalculo) {
		this.listaRecalculo = listaRecalculo;
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
