package claro.com.pe.facturacion.bean;

import java.io.Serializable;

public class Subscriptions implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String producttype;
	private String subscriptionStatus;
	private String cicloFacturacion;
	
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
	
	
	

}
