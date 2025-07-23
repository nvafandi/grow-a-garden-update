package grow.a.garden.controller;

import grow.a.garden.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    ResponseEntity<Object> getWeather() {
        var response = weatherService.getWeather();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/syncWeather")
    ResponseEntity<Object> synchWeather() {
        var response = weatherService.getUpdateWeather();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

}
