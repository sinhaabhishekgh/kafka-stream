package com.example.trade.security.listener;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.trade.security.entity.TradeEventInput;
import com.example.trade.security.entity.TradeEventOutput;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
public class TradeEventProcesserTest {

	
	private static final Logger log = LoggerFactory.getLogger(TradeEventProcesserTest.class);

	
	static final String INPUT_TOPIC = EventChannel.INPUT;
	static final String OUTPUT_TOPIC = EventChannel.OUTPUT;
	
	final Serde<String> stringSerde = Serdes.String();
	final JsonSerde<TradeEventInput> inputValSerde = new JsonSerde<>(TradeEventInput.class);
	final JsonSerde<TradeEventOutput> outputValSerde = new JsonSerde<>(TradeEventOutput.class);
	
	@Value(value = "${test.sample.input.file.path}")
	private String inputFilePath;
	
	@Value(value = "${test.sample.expected.out.file.path}")
	private String outFilePath;
	
	@Value(value = "${test.sample.unorder.input.file.path}")
	private String inputFilePathUnorder;
	
	@Value(value = "${test.sample.unorder.expected.out.file.path}")
	private String outFilePathUnOrder;
	
	@Autowired
	private EventStreamListener eventStreamListener;
	
	@Autowired
	private TradePositionManager manager;
	
	TopologyTestDriver testDriver;
	
	ConsumerRecordFactory<String, String> factory = new ConsumerRecordFactory<>(stringSerde.serializer(), stringSerde.serializer());
	
	static Properties getStreamsConfiguration() {
	    final Properties streamsConfiguration = new Properties();
	    streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "TopologyTestDriver");
	    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
	    return streamsConfiguration;
	  }

	
	List<String> inputMsg = new ArrayList<String>();
	Map<String, TradeEventOutput> expectedOutputMap = new HashMap<String, TradeEventOutput>();
	
	List<String> inputMsgUnOrder = new ArrayList<String>();
	Map<String, TradeEventOutput> expectedOutputMapUnOrder = new HashMap<String, TradeEventOutput>();
	
		
	
	@Before
	public void setup() {
			
		final StreamsBuilder builder = new StreamsBuilder();
		KStream<String, TradeEventInput> input = builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, inputValSerde));
		KStream<String, TradeEventOutput> output = eventStreamListener.process(input);
		output.to(OUTPUT_TOPIC, Produced.with(stringSerde, outputValSerde));
		testDriver = new TopologyTestDriver(builder.build(), getStreamsConfiguration());
		inputMsg  = Utility.getInputList(inputFilePath);
		expectedOutputMap = Utility.loadExpecOutput(outFilePath);
		inputMsgUnOrder  = Utility.getInputList(inputFilePathUnorder);
		expectedOutputMapUnOrder = Utility.loadExpecOutput(outFilePathUnOrder);
		System.out.println("Done loading");
	}
	
	private ProducerRecord<String, TradeEventOutput> readOutput(){
		return testDriver.readOutput(OUTPUT_TOPIC, stringSerde.deserializer(), outputValSerde.deserializer());
				
	}
	
	
	@After
	public void closeConn() {
		try {
			testDriver.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	@Test
	public void eventsInOrderTest() {
		
		Map<String, TradeEventOutput> result = new HashMap<String, TradeEventOutput>();
		for (String event : inputMsg) {
			log.info( String.format("Event msg posted %s", event));
			testDriver.pipeInput(factory.create(INPUT_TOPIC, null, event));
			ProducerRecord<String, TradeEventOutput> outputRecord = readOutput();
			result.put(outputRecord.key(), outputRecord.value());
		}
		
		for (Map.Entry<String, TradeEventOutput> entry : expectedOutputMap.entrySet()) {
			assertEquals(entry.getValue().getQuantity(), result.get(entry.getKey()).getQuantity());
			assertEquals(entry.getValue().getInstrument(), result.get(entry.getKey()).getInstrument());
			assertEquals(entry.getValue().getTrades(), result.get(entry.getKey()).getTrades());
		}
		manager.reset();
		System.out.println(">>>>>>>All Assert Passed");
		
	}
	
	
	@Test
	public void eventsUnOrderedTest() {
		
		Map<String, TradeEventOutput> result = new HashMap<String, TradeEventOutput>();
		for (String event : inputMsgUnOrder) {
			log.info( String.format("Event msg posted %s", event));
			testDriver.pipeInput(factory.create(INPUT_TOPIC, null, event));
			ProducerRecord<String, TradeEventOutput> outputRecord = readOutput();
			result.put(outputRecord.key(), outputRecord.value());
		}
		for (Map.Entry<String, TradeEventOutput> entry : expectedOutputMapUnOrder.entrySet()) {
			assertEquals(entry.getValue().getQuantity(), result.get(entry.getKey()).getQuantity());
			assertEquals(entry.getValue().getInstrument(), result.get(entry.getKey()).getInstrument());
			assertEquals(entry.getValue().getTrades(), result.get(entry.getKey()).getTrades());
		}
		System.out.println(">>>>>>>All Assert Passed");
		
	}
	
	
	
	
	
}
