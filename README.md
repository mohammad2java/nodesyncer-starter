#NodeSync 
----------------------------
	
	This is spring boot starter work for node sync for data consistency
	This will work only working if ALB follow round robin rules
	
how to use this;
--------------------------
		1) add starter into pom.
		2) design spring bean for NodeSyncer interface
		3) NodeUils.pushToSync(Map<String, String> data) 
		called whenever you want to be sync now.
		
		payload data will be Map<String, String> data 
		and mandatory key will be 
		data.put("beanName","<spring bean for NodeSyncer interface>")
		
	   important application.properties with default value
	   -----------------------------------------------------
	   ns.config.enabled=true
	   ns.config.nodes.count=2
	   ns.config.allowed.second=1
	   ns.config.domain=http://localhost:8080




