package claro.com.pe.evalua.bono.recalculo.bean;

import java.io.Serializable;

public class PartyOrd implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String description;
	private String startDateTime;
	private String interactionStatus;
	private String name;
	private String reasonCode;
	private String reason;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getInteractionStatus() {
		return interactionStatus;
	}
	public void setInteractionStatus(String interactionStatus) {
		this.interactionStatus = interactionStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	

}
