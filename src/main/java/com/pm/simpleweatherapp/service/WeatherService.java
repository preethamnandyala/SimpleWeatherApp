package com.pm.simpleweatherapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.simpleweatherapp.exception.WeatherDataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public WeatherService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Retryable(value = {WeatherDataException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public JsonNode getWeather(double latitude, double longitude) {

        String weatherUrl = String.format(
                "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
                latitude, longitude, apiKey
        );

        try {
            String weatherResponse = restTemplate.getForObject(weatherUrl, String.class);
            return objectMapper.readTree(weatherResponse);
        } catch (Exception e) {
            throw new WeatherDataException("Error fetching weather data", e);
        }


    }
}
