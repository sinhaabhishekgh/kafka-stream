package com.example.trade.security.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

import com.example.trade.security.entity.TradeEventOutput;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {

	public static Map<String, TradeEventOutput> loadExpecOutput(String outFilePath) {

		InputStream resource;
		List<TradeEventOutput> expectedOutput = new ArrayList<TradeEventOutput>();
		Map<String, TradeEventOutput> expectedOutputMap = null;
		try {

			ObjectMapper mapper = new ObjectMapper();

			resource = new ClassPathResource(outFilePath).getInputStream();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
				expectedOutput = reader.lines().map(s -> {
					try {
						return mapper.readValue(s, TradeEventOutput.class);
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}).collect(Collectors.toList());
			}
			expectedOutputMap = expectedOutput.stream().collect(Collectors.toMap(TradeEventOutput::getKey, s -> s));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedOutputMap;
	}

	public static List<String> getInputList(String inputFilePath) {
		InputStream resource;
		List<String> inputMsg = new ArrayList<String>();
		try {
			resource = new ClassPathResource(inputFilePath).getInputStream();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
				inputMsg = reader.lines().collect(Collectors.toList());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputMsg;
	}

}
