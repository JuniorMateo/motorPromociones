package claro.com.pe.incontrato.bean;

import java.io.Serializable;
public class BonoProgram implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String program_id;
	private String contractidExterno;
	private String linea;
	private String customerId;
	
	
	public String getProgram_id() {
		return program_id;
	}
	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	public String getContractidExterno() {
		return contractidExterno;
	}
	public void setContractidExterno(String contractidExterno) {
		this.contractidExterno = contractidExterno;
	}
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	
}
