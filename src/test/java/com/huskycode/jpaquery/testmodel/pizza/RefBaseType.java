package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RefBaseType {
	@Id
	private String baseTypeCode;
	
	private Double baseTypePrice;
	private String baseTypeDescription;
	
	public RefBaseType() {
	}
	
	public RefBaseType(String baseTypeCode, Double baseTypePrice,
			String baseTypeDescription) {
		this.baseTypeCode = baseTypeCode;
		this.baseTypePrice = baseTypePrice;
		this.baseTypeDescription = baseTypeDescription;
	}

	public String getBaseTypeCode() {
		return baseTypeCode;
	}
	public void setBaseTypeCode(String baseTypeCode) {
		this.baseTypeCode = baseTypeCode;
	}
	public Double getBaseTypePrice() {
		return baseTypePrice;
	}
	public void setBaseTypePrice(Double baseTypePrice) {
		this.baseTypePrice = baseTypePrice;
	}
	public String getBaseTypeDescription() {
		return baseTypeDescription;
	}
	public void setBaseTypeDescription(String baseTypeDescription) {
		this.baseTypeDescription = baseTypeDescription;
	}	
}
