package com.mohd.node.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class ExpiredRequestScheduler {

	@Value("${ns.config.evit.mins:5}")
	private long evitMins;

	
	@Scheduled(fixedDelayString = "${ns.config.req.gc:30}", timeUnit = TimeUnit.SECONDS)
	public void removeUnwantedRequest() {
		Map<Long, LocalDateTime> map = TrackDataUtil.getRequestIdDataMap();
		List<Long> removalIds = map.entrySet().stream().filter(s -> {
			LocalDateTime value = s.getValue();
			LocalDateTime now = LocalDateTime.now().minusMinutes(evitMins);
			if (now.isAfter(value)) {
				return true;
			}
			return false;
		}).map(s -> s.getKey()).collect(Collectors.toList());
		log.info("list of evicts requestId: {} ", removalIds);
		removalIds.stream().distinct().forEach(s -> {
			map.remove(s);
		});

	}

}
