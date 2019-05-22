package claro.com.pe.evalua.bono.recalculo.bean;

import java.io.Serializable;
import java.util.List;

public class RetrieveSubscriptions implements Serializable {
	private static final long serialVersionUID = 1L;
	private String status;
	private String codeResponse;
	private List<Subscriptions> listaSubscriptions;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCodeResponse() {
		return codeResponse;
	}
	public void setCodeResponse(String codeResponse) {
		this.codeResponse = codeResponse;
	}
	public List<Subscriptions> getListaSubscriptions() {
		return listaSubscriptions;
	}
	public void setListaSubscriptions(List<Subscriptions> listaSubscriptions) {
		this.listaSubscriptions = listaSubscriptions;
	}
	
	

}
