package com.example.trade.security.listener;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.example.trade.security.entity.TradeEventInput;
import com.example.trade.security.entity.TradeEventOutput;


public interface EventChannel {

	
	public static final String  INPUT = "input";
	
	public static final String  OUTPUT = "output";
	
	@Input(INPUT)
	KStream<String, TradeEventInput> in();
		
	@Output(OUTPUT)
	KStream<String, TradeEventOutput> out();
	
	
}
