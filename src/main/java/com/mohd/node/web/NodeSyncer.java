package com.mohd.node.web;

import java.util.Map;

public interface NodeSyncer {
	void doSyncWork(Map<String,String> payload);
}
