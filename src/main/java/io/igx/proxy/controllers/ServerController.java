package io.igx.proxy.controllers;

import java.util.Map;

import io.igx.proxy.domain.ConnectionStats;
import io.igx.proxy.domain.CreateProxyRequest;
import io.igx.proxy.domain.ProxyDefinition;
import io.igx.proxy.services.NettyServerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vinicius Carvalho
 */
@RestController
@RequestMapping(value = "/servers")
public class ServerController {

	private NettyServerService service;

	@Autowired
	public ServerController(NettyServerService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createServer(@RequestBody CreateProxyRequest createProxyRequest){
		ProxyDefinition definition = service.createServer(createProxyRequest);
		ResponseEntity response = new ResponseEntity(definition, HttpStatus.CREATED);
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
	public ResponseEntity getProxyDefinition(@PathVariable("id") String id){
		return new ResponseEntity(service.getProxyDefinition(id),HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity listServers(){
		return new ResponseEntity(service.listServers(),HttpStatus.OK);
	}
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/status")
	public ResponseEntity toggle(@RequestBody Map<String,Object> status, @PathVariable("id") String id){
		Boolean active = (Boolean) status.get("active");
		if(!active){
			service.stopServer(id);
		}else{
			service.startServer(id);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/stats")
	public ResponseEntity getStats(@PathVariable("id") String id){
		ConnectionStats stats = service.getStats(id);
		return new ResponseEntity(stats,HttpStatus.OK);
	}

}
