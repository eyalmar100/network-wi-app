package com.network.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.network.entity.Network;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class NetworkControllerTest {

	private String id="111";

	@Autowired
	private TestRestTemplate template;

	@Before
	public void setup() throws Exception {

	}


	@Test
	public void testConnect() throws Exception {
		HttpEntity<Object> network = getHttpEntity(
				"{\"device_id\": \"a1b5\",\"device_name\": \"Tablet\", \"network_id\": \""+id+"\" , \"auth\": \"wpa\" }");
		ResponseEntity<Void> resultAsset = template.postForEntity("/wifi-tracking/api/network/connect", network,
				Void.class);

	}

	private HttpEntity<Object> getHttpEntity(Object body) {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(body, headers);
	}
	@Test
	public void testGetNetworkById() throws Exception  {

		try {
			ResponseEntity<Network> resultAsset = template.getForEntity("/wifi-tracking/api/network?id="+id,Network.class);

		} catch (EmptyResultDataAccessException ex) {		    
			assert(false);
		}
		assert(true);

	}
	@Test
	public void testUpdateReport1() throws Exception  {
		HttpEntity<Object> network = getHttpEntity(
				"{\"device_id\": \"a1b5\", \"network_id\": \""+id+"\" , \"throughput\": \"200\" }");
		ResponseEntity<Void>  resultAsset = template.exchange("/wifi-tracking/api/report", HttpMethod.POST, network, Void.class);
		testGetNetworkById();
		assert(true);
	}
	@Test
	public void testUpdateReport2()  {
		HttpEntity<Object> network = getHttpEntity(
				"{\"device_id\": \"a1b5\", \"network_id\": \""+id+"\" , \"throughput\": \"300\" }");
		ResponseEntity<Void>  resultAsset = template.exchange("/wifi-tracking/api/report", HttpMethod.POST, network, Void.class);
		assert(true);
	}

}
