package claro.com.pe.facturacion.bean;

import java.io.Serializable;
public class BonoProgram implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String programid;
	private String contractidExterno;
	private String dnnum;
	private String ciclofacturacion;
	
	
	
	public String getProgramid() {
		return programid;
	}
	public void setProgramid(String programid) {
		this.programid = programid;
	}
	public String getContractidExterno() {
		return contractidExterno;
	}
	public void setContractidExterno(String contractidExterno) {
		this.contractidExterno = contractidExterno;
	}
	public String getDnnum() {
		return dnnum;
	}
	public void setDnnum(String dnnum) {
		this.dnnum = dnnum;
	}
	public String getCiclofacturacion() {
		return ciclofacturacion;
	}
	public void setCiclofacturacion(String ciclofacturacion) {
		this.ciclofacturacion = ciclofacturacion;
	}
	
	
}
