package by.bsu.wialontransport.configuration;

import by.bsu.wialontransport.service.simplifyingtrack.simplifier.RamerDouglasPeuckerCoordinatesSimplifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackSimplifyingConfiguration {

    @Bean
    public RamerDouglasPeuckerCoordinatesSimplifier ramerDouglasPeuckerTrackSimplifier(
            @Value("#{new Double('${track-simplifier.ramer-douglas-peucker.epsilon}')}") final double epsilon) {
        return new RamerDouglasPeuckerCoordinatesSimplifier(epsilon);
    }

}
