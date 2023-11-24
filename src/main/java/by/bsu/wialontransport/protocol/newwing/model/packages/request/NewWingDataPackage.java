package by.bsu.wialontransport.protocol.newwing.model.packages.request;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
public final class NewWingDataPackage extends NewWingRequestPackage {
    private final List<NewWingData> data;

    public NewWingDataPackage(final int checksum, final List<NewWingData> data) {
        super(checksum);
        this.data = data;
    }

    //TODO: test
    public Stream<NewWingData> getDataStream() {
        return this.data.stream();
    }
}
