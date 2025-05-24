package com.pm.simpleweatherapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.simpleweatherapp.exception.LocationFromIpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Ipservice {

    private static final Logger logger = LoggerFactory.getLogger(Ipservice.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Ipservice(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode getLocation(String ipAddress) {

        String locationaUrl = "https://ipapi.co/" + ipAddress + "/json";

        try{
            logger.info("Getting location for ip address: " + ipAddress);
            String response = restTemplate.getForObject(locationaUrl, String.class);
            return objectMapper.readTree(response);
        }catch(Exception e){
            logger.error("Error fetching data for ip address: " + ipAddress, e);
            throw new LocationFromIpException("Error fetching data for IP address " + ipAddress, e);
        }

    }
}
