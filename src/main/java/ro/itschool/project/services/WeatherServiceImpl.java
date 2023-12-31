package ro.itschool.project.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import ro.itschool.project.models.Weather;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherValidatorService weatherValidatorService;
    private final ObjectMapper objectMapper;

    public WeatherServiceImpl(WeatherValidatorService weatherValidatorService, ObjectMapper objectMapper) {
        this.weatherValidatorService = weatherValidatorService;
        this.objectMapper = objectMapper;
    }

    public Weather getWeather(String city) throws IOException {
        weatherValidatorService.validateCity(city);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.weatherapi.com/v1/current.json?key=c95443508af34df68be161532231011&q=" + city)
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String cityName = jsonNode.get("location").get("name").asText();
        String description = jsonNode.get("current").get("condition").get("text").asText();
        String lastUpdatedVal = jsonNode.get("current").get("last_updated").asText();
        String temperatureCelsius = jsonNode.get("current").get("temp_c").asText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime lastUpdated = LocalDateTime.parse(lastUpdatedVal, formatter);

        Weather weather = new Weather();
        weather.setCity(cityName);
        weather.setDescription(description);
        weather.setLastUpdated(lastUpdated);
        weather.setTemperatureCelsius(temperatureCelsius);


        return weather;
    }
}