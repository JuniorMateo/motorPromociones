package claro.com.pe.evalua.bono.recalculo.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductOffer implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String description;
	private String startDateTime;
	private String partyProfileTypeName;
	private String productProductStatus;
	private String Pobasic;
	private String units;
	private BigDecimal amount;
	
	
	
	public String getPobasic() {
		return Pobasic;
	}
	public void setPobasic(String pobasic) {
		Pobasic = pobasic;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPartyProfileTypeName() {
		return partyProfileTypeName;
	}
	public void setPartyProfileTypeName(String partyProfileTypeName) {
		this.partyProfileTypeName = partyProfileTypeName;
	}
	public String getProductProductStatus() {
		return productProductStatus;
	}
	public void setProductProductStatus(String productProductStatus) {
		this.productProductStatus = productProductStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
	
}
