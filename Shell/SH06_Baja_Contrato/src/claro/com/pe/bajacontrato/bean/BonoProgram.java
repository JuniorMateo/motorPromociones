package claro.com.pe.bajacontrato.bean;

import java.io.Serializable;
public class BonoProgram implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String programid;
	private String dnnum;
	private String ciclofacturacion;
	private String contractidext;
	
	
	public String getContractidext() {
		return contractidext;
	}
	public void setContractidext(String contractidext) {
		this.contractidext = contractidext;
	}
	public String getProgramid() {
		return programid;
	}
	public void setProgramid(String programid) {
		this.programid = programid;
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
