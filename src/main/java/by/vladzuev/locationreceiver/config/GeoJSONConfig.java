package by.vladzuev.locationreceiver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

@Configuration
public class GeoJSONConfig {

    @Bean
    public GeoJSONReader geoJSONReader() {
        return new GeoJSONReader();
    }

    @Bean
    public GeoJSONWriter geoJSONWriter() {
        return new GeoJSONWriter();
    }
}
