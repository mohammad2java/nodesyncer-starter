package com.mohd.node.web;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.mohd.node.model.PayloadModel;
import com.mohd.node.model.PayloadModelEvent;

@Component
public class NodeUtils {
	
	private static ApplicationEventPublisher publisher;

	@Autowired
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		NodeUtils.publisher = applicationEventPublisher;
	}

	public static void pushToSync(Map<String, String> data) {
		if (Objects.isNull(data) || data.isEmpty()) {
			throw new RuntimeException("Data is Null or Empty");
		}
		if (Objects.isNull(data.get("beanName"))) {
			throw new RuntimeException("beanName is missing");
		}
		PayloadModel payloadModel = PayloadModel.builder().data(data).build();
		PayloadModelEvent event = PayloadModelEvent.builder().payloadModel(payloadModel).build();
		publisher.publishEvent(event);
	}

}
