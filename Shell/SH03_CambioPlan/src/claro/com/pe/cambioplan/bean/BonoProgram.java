package claro.com.pe.cambioplan.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class BonoProgram implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private String contractid;
	private String contractidExterno;
	private String dnnum;
	private String pobasic;
	private String bonoid;
	private String campania;
	private String programid;
	private BigDecimal amount;
	
	
	
	public String getProgramid() {
		return programid;
	}
	public void setProgramid(String programid) {
		this.programid = programid;
	}
	public String getCampania() {
		return campania;
	}
	public void setCampania(String campania) {
		this.campania = campania;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getContractidExterno() {
		return contractidExterno;
	}
	public void setContractidExterno(String contractidExterno) {
		this.contractidExterno = contractidExterno;
	}
	public String getContractid() {
		return contractid;
	}
	public void setContractid(String contractid) {
		this.contractid = contractid;
	}
	public String getDnnum() {
		return dnnum;
	}
	public void setDnnum(String dnnum) {
		this.dnnum = dnnum;
	}
	public String getPobasic() {
		return pobasic;
	}
	public void setPobasic(String pobasic) {
		this.pobasic = pobasic;
	}
	public String getBonoid() {
		return bonoid;
	}
	public void setBonoid(String bonoid) {
		this.bonoid = bonoid;
	}
	
	
}
