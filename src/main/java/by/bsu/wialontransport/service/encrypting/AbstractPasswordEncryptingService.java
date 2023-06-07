package by.bsu.wialontransport.service.encrypting;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.service.AbstractCRUDService;
import by.bsu.wialontransport.service.encrypting.model.Encryptable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractPasswordEncryptingService<E extends AbstractDto<?> & Encryptable> {
    private final BCryptPasswordEncoder encoder;
    private final AbstractCRUDService<?, ?, E, ?, ?> crudService;

    public final E save(final E source) {
        final E encrypted = this.mapToDtoWithEncryptedPassword(source);
        return this.crudService.save(encrypted);
    }

    public final List<E> saveAll(final List<E> source) {
        final List<E> encrypted = this.mapToDtoWithEncryptedPassword(source);
        return this.crudService.saveAll(encrypted);
    }

    public final E updatePassword(final E source, final String rawNewPassword) {
        final String encryptedNewPassword = this.encoder.encode(rawNewPassword);
        return this.updatePassword(this.crudService, source, encryptedNewPassword);
    }

    protected abstract E createWithEncryptedPassword(final E source, final String encryptedPassword);

    protected abstract E updatePassword(final AbstractCRUDService<?, ?, E, ?, ?> crudService,
                                        final E source,
                                        final String encryptedNewPassword);

    private E mapToDtoWithEncryptedPassword(final E source) {
        final String rawPassword = source.getPassword();
        final String encryptedPassword = this.encoder.encode(rawPassword);
        return this.createWithEncryptedPassword(source, encryptedPassword);
    }

    private List<E> mapToDtoWithEncryptedPassword(final List<E> source) {
        return source.stream()
                .map(this::mapToDtoWithEncryptedPassword)
                .toList();
    }
}
