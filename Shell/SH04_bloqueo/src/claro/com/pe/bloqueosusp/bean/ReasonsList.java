package claro.com.pe.bloqueosusp.bean;

import java.io.Serializable;

public class ReasonsList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String activo;
	private String tickStatus;
	private String idReason;
	private String Reason;
	private String tickShdes;
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	public String getTickStatus() {
		return tickStatus;
	}
	public void setTickStatus(String tickStatus) {
		this.tickStatus = tickStatus;
	}
	public String getIdReason() {
		return idReason;
	}
	public void setIdReason(String idReason) {
		this.idReason = idReason;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public String getTickShdes() {
		return tickShdes;
	}
	public void setTickShdes(String tickShdes) {
		this.tickShdes = tickShdes;
	}
	
	

}
