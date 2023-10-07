package com.mohd.node.web;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mohd.node.model.PayloadModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/node/v1")
@Slf4j
@RequiredArgsConstructor
public class NodeSyncController {
	private final ApplicationContext applicationContext;

	private String nodeId = TrackDataUtil.nodeId();
	private Map<Long, LocalDateTime> reqTimeStamp = TrackDataUtil.getRequestIdDataMap();

	@PostMapping("/sync")
	public ResponseEntity<PayloadModel> synNode(@RequestBody PayloadModel payloadModel) {
		log.debug("call receive with payload: {} ",payloadModel);
		PayloadModel ret = PayloadModel.builder().acknowledged(false).requestId(payloadModel.getRequestId())
				.nodeId(nodeId).build();
		if (isSourceDestinationSame(payloadModel)) {
			return ResponseEntity.ok(ret);
		}

		if (reqTimeStamp.containsKey(payloadModel.getRequestId())) {
			ret.setAcknowledged(true);
			return ResponseEntity.ok(ret);
		}
		try {
			NodeSyncer bean = (NodeSyncer) applicationContext.getBean(payloadModel.getData().get("beanName"));
			CompletableFuture<Void> asynCall = CompletableFuture
					.runAsync(() -> bean.doSyncWork(payloadModel.getData()));
			asynCall.whenComplete((ok, err) -> {
				if (Objects.nonNull(err)) {
					log.error("something went wrong: {} ", err.toString());
					return;
				}
				log.info("completed bean.doSyncWork on node:{} ", nodeId);
			});

			ret.setAcknowledged(true);
		} catch (Exception e) {
			log.error("invalid bean name: {}", e.toString());

		}
		return ResponseEntity.ok(ret);
	}

	private boolean isSourceDestinationSame(PayloadModel payloadModel) {
		return StringUtils.equalsIgnoreCase(nodeId, payloadModel.getNodeId());
	}

}
