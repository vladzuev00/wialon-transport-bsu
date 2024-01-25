package by.bsu.wialontransport.configuration.property.protocolserver;

public final class NewWingProtocolServerConfiguration extends ProtocolServerConfiguration {

    public NewWingProtocolServerConfiguration(final String host,
                                              final int port,
                                              final int threadCountProcessingConnection,
                                              final int threadCountProcessingData,
                                              final int connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
