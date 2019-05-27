package com.example.trade.security.listener;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.trade.security.entity.TradeEventInput;
import com.example.trade.security.entity.TradeEventOutput;

@Component
public class TradeEventProcesser implements KeyValueMapper<String, TradeEventInput, KeyValue<String, TradeEventOutput>>{

	private static final Logger log = LoggerFactory.getLogger(TradeEventProcesser.class);

	@Autowired
	private TradePositionManager tradePositionManager;
	
	@Override
	public KeyValue<String, TradeEventOutput> apply(String key, TradeEventInput value) {
		
		log.info(String.format("Message received for Account %s, security identifier %s",value.getAccount(), value.getSecurityIdentifier() ));
		
		String newKey = value.getAccount()+"@@"+value.getSecurityIdentifier();
		TradeEventOutput eventOutput = tradePositionManager.processTradeEvent(value);
		return new KeyValue<String, TradeEventOutput>(newKey, eventOutput);
	}

	

}
