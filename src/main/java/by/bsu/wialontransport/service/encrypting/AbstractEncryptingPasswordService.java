package by.bsu.wialontransport.service.encrypting;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.service.AbstractCRUDService;
import by.bsu.wialontransport.service.encrypting.crud.PasswordEncryptingService;
import by.bsu.wialontransport.service.encrypting.model.Encryptable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEncryptingPasswordService<
        E extends AbstractDto<?> & Encryptable,
        S extends AbstractCRUDService<?, ?, E, ?, ?> & PasswordEncryptingService<E>
        > {
    private final BCryptPasswordEncoder encoder;
    private final S crudService;

    public final E save(final E source) {
        final E encrypted = this.mapToDtoWithEncryptedPassword(source);
        return this.crudService.save(encrypted);
    }

    public final List<E> saveAll(final List<E> source) {
        final List<E> encrypted = this.mapToDtoWithEncryptedPassword(source);
        return this.crudService.saveAll(encrypted);
    }

    public final E update(final E source) {
        final E encrypted = this.mapToDtoWithEncryptedPassword(source);
        return this.crudService.update(encrypted);
    }

    public final E updatePassword(final E source, final String rawNewPassword) {
        final String encryptedNewPassword = this.encoder.encode(rawNewPassword);
        return this.crudService.updatePassword(source, rawNewPassword);
    }

    protected abstract E createWithEncryptedPassword(final E source, final String encryptedPassword);

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
