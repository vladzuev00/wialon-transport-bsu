package by.bsu.wialontransport.config;

import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import by.bsu.wialontransport.protocol.jt808.decoder.JT808PackageDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProtocolConfig {

    @Bean
    public BinaryProtocolDecoder jt808ProtocolDecoder(final List<JT808PackageDecoder> packageDecoders) {
        return new BinaryProtocolDecoder(packageDecoders);
    }
}
