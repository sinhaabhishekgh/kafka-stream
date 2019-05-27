package com.example.trade.security.entity;

import java.io.Serializable;

public class TradeEventInput implements Serializable{

	private static final long serialVersionUID = 7716464283976394664L;

	private Long tradeId;
	
	private Integer version;
	
	private String securityIdentifier;
	
	private Long tradeQuantity;
	
	private String account;
	
	private String operation;
	
	private String direction;

	public TradeEventInput() {
		super();
	}

	public TradeEventInput(Long tradeId, Integer version, String securityIdentifier, Long tradeQuantity, String account) {
		super();
		this.tradeId = tradeId;
		this.version = version;
		this.securityIdentifier = securityIdentifier;
		this.tradeQuantity = tradeQuantity;
		this.account = account;
	}

	public Long getTradeId() {
		return tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSecurityIdentifier() {
		return securityIdentifier;
	}

	public void setSecurityIdentifier(String securityIdentifier) {
		this.securityIdentifier = securityIdentifier;
	}

	public Long getTradeQuantity() {
		return tradeQuantity;
	}

	public void setTradeQuantity(Long tradeQuantity) {
		this.tradeQuantity = tradeQuantity;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	

	
}
