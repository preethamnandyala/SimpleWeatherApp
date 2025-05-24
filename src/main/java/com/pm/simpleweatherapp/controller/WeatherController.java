package com.pm.simpleweatherapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pm.simpleweatherapp.dto.WeatherDto;
import com.pm.simpleweatherapp.exception.ClientIpException;
import com.pm.simpleweatherapp.service.Ipservice;
import com.pm.simpleweatherapp.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private final Ipservice ipService;
    @Autowired
    private final WeatherService weatherService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public WeatherController(Ipservice ipService, WeatherService weatherService) {
        this.ipService = ipService;
        this.weatherService = weatherService;
    }

    @GetMapping("api/weather")
    public ResponseEntity<WeatherDto> getWeather(HttpServletRequest request) {
        try {
            String ipAddress = getClientIp(request);
            if (ipAddress == null || ipAddress.isEmpty()) {
                throw new ClientIpException("IP address is null or empty!");
            }

            JsonNode locationJson = ipService.getLocation(ipAddress);
            String city = locationJson.path("city").asText();
            double latitude = locationJson.path("latitude").asDouble();
            double longitude = locationJson.path("longitude").asDouble();

            JsonNode weatherJsonNode = weatherService.getWeather(latitude, longitude);

            logger.info("Weather JSON: {}", weatherJsonNode.toPrettyString());

            double temperature = weatherJsonNode.path("main").path("temp").asDouble();

            WeatherDto weatherDto = WeatherDto.builder()
                    .clientIp(ipAddress)
                    .location(city)
                    .greeting("Hello, the temperature is " + temperature + " degree Celsius in " + city + "!")
                    .build();

            logger.info("Successfully retrieved weather data for IP: {}", ipAddress);
            return ResponseEntity.ok(weatherDto);

        } catch (Exception e) {
            logger.error("Error occurred while fetching weather data", e);
            throw e;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Forwarded-For");
        if (remoteAddr == null || remoteAddr.isEmpty()) {
            remoteAddr = request.getRemoteAddr();
        } else {
            remoteAddr = remoteAddr.split(",")[0];
        }
        return remoteAddr;
    }
}
