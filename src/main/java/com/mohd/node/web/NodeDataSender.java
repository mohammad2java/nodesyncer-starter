package com.mohd.node.web;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mohd.node.model.PayloadModel;
import com.mohd.node.model.PayloadModelEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableAsync
@Slf4j
public class NodeDataSender {
	
	@Value("${ns.config.domain:http://localhost:8080}")
	private String domain;
	
	@Autowired
	@Qualifier("nodeRestTemplate")
	private RestTemplate nodeRestTemplate;
	
	@Value("${ns.config.nodes.count:2}")
	private int nodesCount;

	@Value("${ns.config.allowed.second:1}")
	private long seconds;

	@EventListener
	@Async(value = "senderES")
	public void listenEvent(PayloadModelEvent event) {
		log.info("event-recived: {}", event);
		HashSet<String> resNodes = new HashSet<String>();
		LocalDateTime allowdTimes = LocalDateTime.now().plusSeconds(seconds);
		while (true) {
			if (resNodes.size() >= (nodesCount - 1)) {
				log.info("reached count-informed all nodes of clusterd");
				break;
			}
			LocalDateTime now = LocalDateTime.now();
			log.info("now: {} allowd: {} ",now,allowdTimes);
			if (now.isAfter(allowdTimes)) {
				log.warn("yet not reached count-break due to over times");
				break;
			}
			String url = buildUrl();
			PayloadModel request = buildReq(event);
			PayloadModel response = nodeRestTemplate.postForObject(url, request, PayloadModel.class);
			if (response.isAcknowledged()) {
				resNodes.add(response.getNodeId());
			}
		
		}

	}

	private PayloadModel buildReq(PayloadModelEvent event) {
		PayloadModel payloadModel = event.getPayloadModel();
		payloadModel.setNodeId(TrackDataUtil.nodeId());
		payloadModel.setRequestId(TrackDataUtil.requestId());
		return payloadModel;
	}

	private String buildUrl() {
		return String.format("%s/node/v1/sync", domain);
	}
	
}
