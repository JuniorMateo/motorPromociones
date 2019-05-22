package claro.com.pe.evalua.bono.recalculo.bean;

import java.io.Serializable;

public class BonoRecalculo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String bonoId;
	private String pobono;
	private String pobononame;
	private String bonodes;
	private String flag	;
	private String grupo;
	private String tipo;
	private String elec;
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getElec() {
		return elec;
	}
	public void setElec(String elec) {
		this.elec = elec;
	}
	public String getBonoId() {
		return bonoId;
	}
	public void setBonoId(String bonoId) {
		this.bonoId = bonoId;
	}
	public String getPobono() {
		return pobono;
	}
	public void setPobono(String pobono) {
		this.pobono = pobono;
	}
	public String getPobononame() {
		return pobononame;
	}
	public void setPobononame(String pobononame) {
		this.pobononame = pobononame;
	}
	public String getBonodes() {
		return bonodes;
	}
	public void setBonodes(String bonodes) {
		this.bonodes = bonodes;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	

}
