package com.nework.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
  
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.network.entity.Device;
import com.network.entity.Network;
import com.network.repository.DeviceRepository;
import com.network.repository.NetworkRepository;
import com.network.service.NetworkService;

 
@RunWith(SpringJUnit4ClassRunner.class)
public class NetworkServiceTest {

	private NetworkService networkService;

	@MockBean
	private NetworkRepository networkRepository;

	@MockBean
	private DeviceRepository deviceRepository;

	@Before
	public void initiate(){
		networkService=new NetworkService();
		networkService.setNetworkRepository(networkRepository);
		networkService.setDeviceRepository(deviceRepository);
	}
	@org.junit.After
	public void destroy(){
		networkService=null;
	}

	@Test
	public void testSaveNetwork(){	

		MockitoAnnotations.initMocks(this);			
		Network network=new Network(1777L,"auth5");

		Mockito.when(networkRepository.saveAndFlush((Network)anyObject())).thenReturn(network);		
		try{
			networkService.saveNewNetwork(network);
			assert(true);
		} catch (Exception e) {
			assert(false);
		}
	}
	

	@Test
	public void testSaveReport(){	

		MockitoAnnotations.initMocks(this);			
		Network network=new Network(1777L,"auth5");
		
		Device device=new Device("13344", "dev1");

		Mockito.when(deviceRepository.findById(anyString())).thenReturn(device);
		Mockito.when(deviceRepository.save((Device)anyObject())).thenReturn(device);	
		
		Mockito.when(networkRepository.getAvgThroughput(anyLong())).thenReturn(new Float(222));
		Mockito.when(networkRepository.updateNetwork(anyLong(),anyFloat())).thenReturn(1777);	
		
 		
		try{
			networkService.report("13344", 1777L, 5555);
			assert(true);
		} catch (Exception e) {
			assert(false);
		}
	}
	

	@Test
	public void testSearch(){	

		MockitoAnnotations.initMocks(this);			
		Network network=new Network(1777L,"auth5");

		Mockito.when(networkRepository.findOne(anyLong())).thenReturn(network);		
		try{
			Network net=networkService.getNetworkById(1777L);
			assertEquals(true, net!= null);
			assertEquals(new Long(1777),net.getId());
		} catch (Exception e) {
			assert(false);
		}
	}  


}

