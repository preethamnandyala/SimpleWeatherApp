package com.pm.simpleweatherapp.exception;

public class LocationFromIpException extends RuntimeException {
    public LocationFromIpException(String message) {
        super(message);
    }

    public LocationFromIpException(String message, Throwable cause) {
        super(message, cause);
    }
}
