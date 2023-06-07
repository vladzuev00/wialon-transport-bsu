package by.bsu.wialontransport.service.encrypting;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.TrackerService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public final class TrackerEncryptingPasswordService extends AbstractEncryptingPasswordService<Tracker, TrackerService> {

    public TrackerEncryptingPasswordService(final BCryptPasswordEncoder encoder, final TrackerService trackerService) {
        super(encoder, trackerService);
    }

    @Override
    protected Tracker createWithEncryptedPassword(final Tracker source, final String encryptedPassword) {
        return new Tracker(
                source.getId(),
                source.getImei(),
                encryptedPassword,
                source.getPhoneNumber(),
                source.getUser()
        );
    }
}
