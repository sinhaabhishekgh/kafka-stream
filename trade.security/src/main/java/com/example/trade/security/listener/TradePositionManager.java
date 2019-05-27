package com.example.trade.security.listener;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.trade.security.entity.Direction;
import com.example.trade.security.entity.Operation;
import com.example.trade.security.entity.TradeEventInput;
import com.example.trade.security.entity.TradeEventOutput;

@Component
public class TradePositionManager {
	
	/**
	 * This variable acting as storage (can be DB, etc)
	 */
	private Map<String, TradeEventOutput> tradePositionDetails;
		

	public TradePositionManager() {
		super();
		this.tradePositionDetails = new HashMap<String, TradeEventOutput>();
	}


	public TradeEventOutput processTradeEvent(TradeEventInput input) {
		
		String key = input.getAccount() + "@@" + input.getSecurityIdentifier();
		
		TradeEventOutput existing = tradePositionDetails.get(key);
		
		if(existing == null) {
			existing = new TradeEventOutput(input.getAccount(), input.getSecurityIdentifier(), BigInteger.ZERO.longValue());
		}
		
		TradeEventOutput result = adjustQuantity(existing, input, key);
		if(result != null)
			tradePositionDetails.put(key, result);

		return tradePositionDetails.get(key);
	}
	
	
	
	private TradeEventOutput adjustQuantity(TradeEventOutput existing, TradeEventInput input, String key) {
		
		if(existing.getLatestVersion() == null || (!existing.getTradeByVersion().containsKey(input.getTradeId())) ||
				(input.getVersion().compareTo(existing.getTradeByVersion().get(input.getTradeId()))>0)) {	
			if(input.getOperation().equals(Operation.CANCEL.name())) {
				populateDetails(existing, 0l, input.getVersion(), input.getTradeId());
				return existing;
			}else if(input.getDirection().equals(Direction.BUY.name()) && 
					(input.getOperation().equals(Operation.NEW.name()) || input.getOperation().equals(Operation.AMEND.name()) )){
				populateDetails(existing, input.getTradeQuantity(), input.getVersion(), input.getTradeId());
				return existing;
			}else {
				Long qty = existing.getQuantity().longValue() - input.getTradeQuantity().longValue();
				populateDetails(existing, qty, input.getVersion(), input.getTradeId());
				return existing;
			}
		}
		return null;
	}
	
	
	private void populateDetails(TradeEventOutput existing, Long qty, Integer version, Long tradeId) {
		existing.setQuantity(qty);
		existing.setLatestVersion(version);
		existing.getTrades().add(tradeId);
		existing.getTradeByVersion().put(tradeId, version);
	}
	
	public void reset() {
		this.tradePositionDetails = new HashMap<String, TradeEventOutput>();
	}
	
}
