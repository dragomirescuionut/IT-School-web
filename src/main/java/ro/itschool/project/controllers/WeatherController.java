package ro.itschool.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.itschool.project.models.Weather;
import ro.itschool.project.services.WeatherService;

import java.io.IOException;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @GetMapping("/api/weather")
    public ResponseEntity<Weather> getResponse(@RequestParam String city) throws IOException {
        return ResponseEntity.ok(weatherService.getWeather(city));
    }
}