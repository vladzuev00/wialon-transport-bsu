package by.vladzuev.locationreceiver.protocol.teltonika.holder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public final class TeltonikaLoginSuccessHolder {
    private volatile boolean success;
}
