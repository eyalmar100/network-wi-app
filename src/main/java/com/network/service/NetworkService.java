package com.network.service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.entity.Device;
import com.network.entity.Network;
import com.network.repository.DeviceRepository;
import com.network.repository.NetworkRepository;

@Service
@Transactional
public class NetworkService {

	private static final Logger logger = Logger.getLogger(NetworkService.class);

	@Autowired
	private NetworkRepository  networkRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	public Network getNetworkById(long networkId){
		Network network=networkRepository.findOne(networkId);
		return network;
	}


	public void createNetwork(long id,String networkName){
		Network wifiNetwork=new Network(id,networkName);
		networkRepository.saveAndFlush(wifiNetwork);
		logger.debug("Network obj was saved to db"); 
	}


	public void update(Network network) {
		Network dbNetwork=networkRepository.getOne(network.getId());
		dbNetwork.setAvgThroughput(network.getAvgThroughput());
		networkRepository.save(dbNetwork);
		logger.debug("Network obj was updated to db"); 

	 
	}
	public void saveNewNetwork(Network network) {

		networkRepository.saveAndFlush(network);

	}
	public void report(String deviceId,long networkId,float throughput){

		 
		Device device=deviceRepository.findById(deviceId);
		device.setThroughput(throughput);
		deviceRepository.save(device);
 		float avgThroughput=networkRepository.getAvgThroughput(networkId);		
		networkRepository.updateNetwork(networkId, avgThroughput);
		 
		
	}


	public NetworkRepository getNetworkRepository() {
		return networkRepository;
	}


	public void setNetworkRepository(NetworkRepository networkRepository) {
		this.networkRepository = networkRepository;
	}


	public DeviceRepository getDeviceRepository() {
		return deviceRepository;
	}


	public void setDeviceRepository(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

}
