package com.nerwork.util;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.network.entity.Device;
import com.network.entity.Network;

public class RestClientUtil {
    
	private static String baseUrl="http://localhost:8080/wifi-tracking/api/network";
	 
	private RestTemplate restTemplate; 
	
	RestClientUtil(){
		 restTemplate = new RestTemplate();
	}
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

    
	
	
	public void getNetworkByIdObj(long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	   	String url = baseUrl+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        ResponseEntity<Network> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Network.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        //networkJson
        Network network = responseEntity.getBody();
        System.out.println("RestClientUtil.getNetworkById():network is "+network);
        List<Device>devices=network.getDevices();
        for(Device device : devices) {
             System.out.println("Id:"+device.getName());
        }
    }
	
     
	 
    
    public void connect(String connectionParams) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
   	    String url = baseUrl+"/connect";
	    
        HttpEntity<String> requestEntity = new HttpEntity<String>(connectionParams, headers);
        restTemplate.put(url, requestEntity);
    }
    
    public void report(String connectionParams) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
   	    String url = baseUrl+"/report";
	    
        HttpEntity<String> requestEntity = new HttpEntity<String>(connectionParams, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);//(url, requestEntity);
        System.out.println("RestClientUtil.enclosing_method()");
    }
     
    
    
   
    public static void main(String args[]) {
    	
    	
    	
    	Network network=new Network(123L,"");
    	Device d1=new Device("s23","samsung");
    	d1.setNetwork(network);
    	Device d2=new Device("w2qa","Siumi");
    	d1.setNetwork(network);
    	List<Device>devices=new ArrayList();
    	devices.add(d1);
    	devices.add(d2);
    	network.setDevices(devices);
    	
    	Network network1=new Network(1234L,"");
    	Device d11=new Device("s23aa","samsung");
    	d11.setNetwork(network1);
    	Device d22=new Device("w2qgga","Siumi");
    	d22.setNetwork(network1);
    	List<Device>devices2=new ArrayList();
    	devices2.add(d11);
    	devices2.add(d22);
    	network1.setDevices(devices2);
    	
    	List<Network>networks=new ArrayList();
    	networks.add(network);
    	networks.add(network1);
    	
    	ObjectMapper om=new ObjectMapper();
        try {
		String s=	om.writerWithDefaultPrettyPrinter().writeValueAsString(networks);
		System.out.println("RestClientUtil.main(): s is "+s);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	
       	
    	
    	
    	     
    	Path path;
    	  StringBuilder data = new StringBuilder();
		try {
			path = Paths.get(RestClientUtil.class.getClassLoader()
				      .getResource("reportParams.json").toURI());
			
			//data = new StringBuilder();
 		    Stream<String> lines = Files.lines(path);
 		    lines.forEach(line -> data.append(line).append("\n"));
 		    lines.close();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		          
    		   
    	
    		    
    		    String connectionParams;//=convertBankToJson("MIZRAHI","TEL-AVIV","WEITZMAN","HOLON","SOKOLOV");
    	
    	RestClientUtil util = new RestClientUtil();
    	util.report(data.toString());
    	//util.getNetworkByIdObj(123456);
        //util.getArticleByIdDemo();
    	//util.connect(data.toString()) ;
    	
    //	jsonBank=convertBankToJson("HAPOALIM","HERZELIA","ADD1","KFAR-SABA","ADD2");
    	
    	//util.addBank(jsonBank);
    	//util.deleteArticleDemo();
    //	util.updateBank();
    	//util.deleteBank();
    	
    	//util.addArticleDemo();
  //  	util.getAllBranches();
    	//util.updateArticleDemo();
    	//util.deleteArticleDemo();
    }    
}
