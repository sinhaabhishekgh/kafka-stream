package com.example.trade.security.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TradeEventOutput implements Serializable{

	private static final long serialVersionUID = -1946969766182628773L;
	
	private String account;
	
	private String instrument;
	
	private Long quantity;
	
	private Integer latestVersion;
	
	private Set<Long> trades;
	
	private Map<Long, Integer> tradeByVersion;
	
	public TradeEventOutput() {
		super();
	}

	public TradeEventOutput(String account, String instrument, Long quantity) {
		super();
		this.account = account;
		this.instrument = instrument;
		this.quantity = quantity;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Set<Long> getTrades() {
		 if(trades == null)
			 trades = new HashSet<Long>();
		 return trades;
			 
	}

	public void setTrades(Set<Long> trades) {
		this.trades = trades;
	}

	public Integer getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(Integer latestVersion) {
		this.latestVersion = latestVersion;
	}
	
	public String getKey() {
		return this.account.concat("@@").concat(instrument);
	}

	public Map<Long, Integer> getTradeByVersion() {
		if(tradeByVersion == null)
			tradeByVersion = new HashMap<Long, Integer>();
		return tradeByVersion;
	}

	public void setTradeByVersion(Map<Long, Integer> tradeByVersion) {
		this.tradeByVersion = tradeByVersion;
	}

	
	
}
