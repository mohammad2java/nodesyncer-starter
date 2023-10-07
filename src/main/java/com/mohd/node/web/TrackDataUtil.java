package com.mohd.node.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TrackDataUtil {
	private static Map<Long, LocalDateTime> requestIdData = new ConcurrentHashMap<>();

	private static String nodeId = UUID.randomUUID().toString();
	
	
	public static Map<Long, LocalDateTime> getRequestIdDataMap() {
		return requestIdData;
	}

	public static String nodeId() {
		return nodeId;
	}

	public static Long requestId() {
		String dateInString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		return Long.valueOf(dateInString);
	}
}
