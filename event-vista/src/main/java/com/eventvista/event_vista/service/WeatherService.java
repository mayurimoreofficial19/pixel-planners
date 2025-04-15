package com.eventvista.event_vista.service;

import com.eventvista.event_vista.model.dto.WeatherData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public WeatherData getWeatherData(String location, LocalDate date) {
        try {
            // Validate inputs
            if (!StringUtils.hasText(location)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location cannot be empty");
            }

            if (date == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date cannot be null");
            }

            // Call OpenWeatherMap API
            String url = String.format("%s?q=%s&appid=%s&units=imperial", weatherApiUrl, location, apiKey);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            // Extract weather data
            String icon = root.path("weather").get(0).path("icon").asText();
            double temp = root.path("main").path("temp").asDouble();
            String description = root.path("weather").get(0).path("description").asText();

            return new WeatherData(
                    icon,
                    String.format("%.1f°F", temp),
                    description
            );
        } catch (ResponseStatusException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching weather data: " + e.getMessage());
        }
    }

    private String getMockIconForDate(LocalDate date) {
        int month = date.getMonthValue();
        if (month >= 3 && month <= 5) return "01d"; // Spring
        if (month >= 6 && month <= 8) return "02d"; // Summer
        if (month >= 9 && month <= 11) return "03d"; // Fall
        return "04d"; // Winter
    }

    private String getMockTemperatureForDate(LocalDate date) {
        int month = date.getMonthValue();
        if (month >= 3 && month <= 5) return "68°F"; // Spring
        if (month >= 6 && month <= 8) return "86°F"; // Summer
        if (month >= 9 && month <= 11) return "59°F"; // Fall
        return "41°F"; // Winter
    }

    private String getMockDescriptionForDate(LocalDate date) {
        int month = date.getMonthValue();
        if (month >= 3 && month <= 5) return "Mild Spring Weather";
        if (month >= 6 && month <= 8) return "Hot Summer Weather";
        if (month >= 9 && month <= 11) return "Cool Fall Weather";
        return "Cold Winter Weather";
    }
}