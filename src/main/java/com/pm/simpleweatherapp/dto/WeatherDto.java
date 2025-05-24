package com.pm.simpleweatherapp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDto {

    private String clientIp;
    private String location;
    private String greeting;

}
