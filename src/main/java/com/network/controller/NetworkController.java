package com.network.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.network.entity.Device;
import com.network.entity.Network;
import com.network.service.DeviceService;
import com.network.service.NetworkService;

@RestController
@RequestMapping("wifi-tracking/api")
public class NetworkController {

    final static Logger logger = Logger.getLogger(NetworkController.class);

    @Autowired
    private NetworkService networkService;

    @Autowired
    private DeviceService deviceDervice;

    @GetMapping("/network")
    public Network getById(@RequestParam("id") Long id) {
        logger.debug(String.format("%s", "resuest for network get by id-starting "));
        Network net = networkService.getNetworkById(id);

        if (net != null) {
            List<Device> devices = net.getDevices();
            net.setDevices(devices);
        }
        logger.debug(String.format("%s", "resuest for connect-ok "));
        return net;
    }

    @PutMapping("/network/connect")
    public ResponseEntity<Void> connect(@RequestBody String payload) {

        logger.debug(String.format("%s", "resuest for connect a new device "));
        JSONObject json;
        try {
            json = new JSONObject(payload);

            String deviceId = json.getString("device_id");
            String deviceName = json.getString("device_name");
            long networkId = json.getLong("network_id");
            String auth = json.getString("auth");
            Network network = networkService.getNetworkById(networkId);
            Device device = new Device(deviceId, deviceName);
            if (network == null) {
                logger.debug(String.format("%s", "creating new network object "));
                network = new Network(networkId, auth);
                handleDevices(network, device);
                network.setAvgThroughput();
                networkService.saveNewNetwork(network);
            } else {
                logger.debug(String.format("%s", "found exiting network in db "));
                handleDevices(network, device);
                networkService.update(network);
            }
            logger.debug(String.format("%s", "resuest for connect a new device -ok"));
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } catch (JSONException e) {

            logger.error(e.getMessage());
        }
        return null;
    }

    @PostMapping("/network/report")
    public ResponseEntity<Void> report(@RequestBody String payload) {
        logger.debug(String.format("%s", "creating new report   "));
        float throughput;
        try {
            JSONObject json = new JSONObject(payload);
            String deviceId = json.getString("device_id");
            long networkId = json.getLong("network_id");
            throughput = Float.valueOf(json.getString("throughput"));
            networkService.report(deviceId, networkId, throughput);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (NumberFormatException e) {

            logger.error(e.getMessage());

        } catch (JSONException e) {

            logger.error(e.getMessage());
        }
        logger.warn(String.format("%s", "some problme with reporting  "));
        return null;
    }

    private void handleDevices(Network network, Device device) {
        List<Device> listDevices = network.getDevices();
        listDevices.add(device);
        device.setNetwork(network);
        network.setAvgThroughput();
    }

    /*
	 * Just Suggestion.. not implemented fully , but seems to be required ..
     */
    @PostMapping("/network/disconnect")
    public ResponseEntity<Void> disconnect(@RequestBody String payload) {

        JSONObject json;

        json = new JSONObject(payload);
        // read params from json to get deviceId, and network id
        // get the network obj and device obj , something like :
        String deviceId = json.getString("device_id");
        String deviceName = json.getString("device_name");
        long networkId = json.getLong("network_id");
        String auth = json.getString("auth");
        Network network = networkService.getNetworkById(networkId);
        Device device = new Device(deviceId, deviceName);
        List<Device> devices = network.getDevices();
        int size = devices.size();
        for (int i = 0; i < size; ++i) {
            if (device.getId().equals(deviceId)) {
                devices.remove(i);
                break;
            }
        }
        //set avg throughput of network..
        // remove device from db ..	
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
