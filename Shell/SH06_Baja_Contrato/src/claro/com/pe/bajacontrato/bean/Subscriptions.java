package claro.com.pe.bajacontrato.bean;

import java.io.Serializable;

public class Subscriptions implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String producttype;
	private String contractID;
	private String subscriptionStatus;
	private String cicloFacturacion;
	private String activo;
	
	
	public String getContractID() {
		return contractID;
	}
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}
	public String getProducttype() {
		return producttype;
	}
	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}
	public String getCicloFacturacion() {
		return cicloFacturacion;
	}
	public void setCicloFacturacion(String cicloFacturacion) {
		this.cicloFacturacion = cicloFacturacion;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	
	
	
	

}
