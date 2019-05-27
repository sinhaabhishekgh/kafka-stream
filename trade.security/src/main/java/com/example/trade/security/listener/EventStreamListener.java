package com.example.trade.security.listener;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

import com.example.trade.security.entity.TradeEventInput;
import com.example.trade.security.entity.TradeEventOutput;

@EnableBinding(EventChannel.class)
public class EventStreamListener {

	@Autowired
	private TradeEventProcesser tradeEventProcesser;
	
	
	@StreamListener(EventChannel.INPUT)
	@SendTo(EventChannel.OUTPUT)
	public KStream<String, TradeEventOutput> process(KStream<String, TradeEventInput> input){
		
		return input.map(tradeEventProcesser);
		
	}
	
	
	
	
	
}
