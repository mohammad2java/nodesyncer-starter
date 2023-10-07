package com.mohd.node;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configurable
@AutoConfiguration
@ConditionalOnProperty(havingValue = "true",prefix = "ns.config",name = "enabled",matchIfMissing = true)
@Slf4j
@ComponentScan("com.mohd.node")
public class NodeSynAutoConfiguration {
	
	@PostConstruct
	public void init() {
		log.info("intialized: {} ",this);
	}
	@Bean
	public RestTemplate nodeRestTemplate() {
		return new RestTemplate();
	}
	
	
	@Bean
	public ExecutorService senderES() {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5, new CustomizableThreadFactory("sender_"));
		return newFixedThreadPool;
	}
	
	
}
