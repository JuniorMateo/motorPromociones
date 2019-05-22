package claro.com.pe.evalua.bono.recalculo.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Subscriptions implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String producttype;
	private String subscriptionStatus;
	private String activo;
	private String billingaccountbase;
	private BigDecimal cargoFijo;
	private String lineabase;
	private String contractIdBase;
	private String poidbase;
	private String ponamebase;
	private String customeridextbase;
	private String ciclofactbase;
	private String cargofijoplanbase;
	private String tipoproductobase;
	private String tiposuscripcionbase;
	private String fecactivacionbase;
	private String estadolineabase;
	
	private String bundleid;
	private String statusbilling;
	private String mail;
	private String tecnologia;
	private String tipotelefono;
	private String fecmodsuscription;
	private String hisbloq;
	private String indlineaportabase;
	private String hiscambplanbase;
	private String hisrenovacionbase;
	private String bolsalineabase;
	private String activacion;
	private String tipoOpera;
	private String estOm;
	
	private String periodo;
	
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getTipoOpera() {
		return tipoOpera;
	}
	public void setTipoOpera(String tipoOpera) {
		this.tipoOpera = tipoOpera;
	}
	public String getEstOm() {
		return estOm;
	}
	public void setEstOm(String estOm) {
		this.estOm = estOm;
	}
	public String getActivacion() {
		return activacion;
	}
	public void setActivacion(String activacion) {
		this.activacion = activacion;
	}
	public String getFecactivacionbase() {
		return fecactivacionbase;
	}
	public void setFecactivacionbase(String fecactivacionbase) {
		this.fecactivacionbase = fecactivacionbase;
	}
	public String getFecmodsuscription() {
		return fecmodsuscription;
	}
	public void setFecmodsuscription(String fecmodsuscription) {
		this.fecmodsuscription = fecmodsuscription;
	}
	public String getBillingaccountbase() {
		return billingaccountbase;
	}
	public void setBillingaccountbase(String billingaccountbase) {
		this.billingaccountbase = billingaccountbase;
	}
	public String getContractIdBase() {
		return contractIdBase;
	}
	public void setContractIdBase(String contractIdBase) {
		this.contractIdBase = contractIdBase;
	}
	public String getPoidbase() {
		return poidbase;
	}
	public void setPoidbase(String poidbase) {
		this.poidbase = poidbase;
	}
	public String getPonamebase() {
		return ponamebase;
	}
	public void setPonamebase(String ponamebase) {
		this.ponamebase = ponamebase;
	}
	public String getCustomeridextbase() {
		return customeridextbase;
	}
	public void setCustomeridextbase(String customeridextbase) {
		this.customeridextbase = customeridextbase;
	}
	public String getCiclofactbase() {
		return ciclofactbase;
	}
	public void setCiclofactbase(String ciclofactbase) {
		this.ciclofactbase = ciclofactbase;
	}
	public String getCargofijoplanbase() {
		return cargofijoplanbase;
	}
	public void setCargofijoplanbase(String cargofijoplanbase) {
		this.cargofijoplanbase = cargofijoplanbase;
	}
	public String getTipoproductobase() {
		return tipoproductobase;
	}
	public void setTipoproductobase(String tipoproductobase) {
		this.tipoproductobase = tipoproductobase;
	}
	public String getTiposuscripcionbase() {
		return tiposuscripcionbase;
	}
	public void setTiposuscripcionbase(String tiposuscripcionbase) {
		this.tiposuscripcionbase = tiposuscripcionbase;
	}
	
	public String getEstadolineabase() {
		return estadolineabase;
	}
	public void setEstadolineabase(String estadolineabase) {
		this.estadolineabase = estadolineabase;
	}
   
	
	public String getLineabase() {
		return lineabase;
	}
	public void setLineabase(String lineabase) {
		this.lineabase = lineabase;
	}
	public BigDecimal getCargoFijo() {
		return cargoFijo;
	}
	public void setCargoFijo(BigDecimal cargoFijo) {
		this.cargoFijo = cargoFijo;
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
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	public String getBundleid() {
		return bundleid;
	}
	public void setBundleid(String bundleid) {
		this.bundleid = bundleid;
	}
	public String getStatusbilling() {
		return statusbilling;
	}
	public void setStatusbilling(String statusbilling) {
		this.statusbilling = statusbilling;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}
	public String getTipotelefono() {
		return tipotelefono;
	}
	public void setTipotelefono(String tipotelefono) {
		this.tipotelefono = tipotelefono;
	}

	public String getHisbloq() {
		return hisbloq;
	}
	public void setHisbloq(String hisbloq) {
		this.hisbloq = hisbloq;
	}
	public String getIndlineaportabase() {
		return indlineaportabase;
	}
	public void setIndlineaportabase(String indlineaportabase) {
		this.indlineaportabase = indlineaportabase;
	}
	public String getHiscambplanbase() {
		return hiscambplanbase;
	}
	public void setHiscambplanbase(String hiscambplanbase) {
		this.hiscambplanbase = hiscambplanbase;
	}
	public String getHisrenovacionbase() {
		return hisrenovacionbase;
	}
	public void setHisrenovacionbase(String hisrenovacionbase) {
		this.hisrenovacionbase = hisrenovacionbase;
	}
	public String getBolsalineabase() {
		return bolsalineabase;
	}
	public void setBolsalineabase(String bolsalineabase) {
		this.bolsalineabase = bolsalineabase;
	}
	
	
	
	
	
	
	

}
