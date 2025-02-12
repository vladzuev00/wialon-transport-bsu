package by.bsu.wialontransport.config;

import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import by.bsu.wialontransport.protocol.core.decoder.TextProtocolDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.jt808.decoder.JT808PackageDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingEventCountPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingLoginPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.location.NewWingLocationPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.encoder.NewWingPackageEncoder;
import by.bsu.wialontransport.protocol.wialon.decoder.WialonPackageDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProtocolConfig {

    @Bean
    public BinaryProtocolDecoder jt808ProtocolDecoder(final List<JT808PackageDecoder> packageDecoders) {
        return new BinaryProtocolDecoder(packageDecoders);
    }

    @Bean
    public BinaryProtocolDecoder newWingProtocolDecoder(final NewWingLoginPackageDecoder loginPackageDecoder,
                                                        final NewWingEventCountPackageDecoder eventCountPackageDecoder,
                                                        final NewWingLocationPackageDecoder locationPackageDecoder) {
        return new BinaryProtocolDecoder(List.of(loginPackageDecoder, eventCountPackageDecoder, locationPackageDecoder));
    }

    @Bean
    public ProtocolEncoder newWingProtocolEncoder(final List<NewWingPackageEncoder<?>> encoders) {
        return new ProtocolEncoder(encoders);
    }

    @Bean
    public TextProtocolDecoder wialonProtocolDecoder(final List<WialonPackageDecoder> packageDecoders) {
        return new TextProtocolDecoder(packageDecoders);
    }
}
